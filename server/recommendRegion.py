from urllib.request import urlopen
import json
try:
  import jpype
  import jpype1
except:
  import jpype
from konlpy.tag import Okt
import pandas as pd
import math
import numpy as np
import operator

def find_recommend_region_from_keyword(my_keyword, url):
  responseBody = urlopen(url).read().decode('utf-8')
  jsonArray = json.loads(responseBody)
  dataArray = jsonArray.get("data")

  okt = Okt()
  d = dict()  # key : 지역명, value : 지역 별 키워드

  for each in dataArray:
    s = each.get("우수사업명")
    keyword = okt.nouns(s)
    region = str(each.get("지역"))

    if region in d:
      d[region] += keyword
    else:
      d[region] = keyword

  # 중복 키워드 제거
  for i in d.keys():
    d[i] = list(set(d[i]))

  # 내가 선택한 키워드를 포함하고 있는 지역 출력
  my_keyword = my_keyword.split(",")
  max_count = 0
  recommend_region = ""
  recommend_regions = []

  # 내가 선택한 키워드를 최대로 많이 포함하고 있는 지역 1개
  for i in d.keys():
    val = set(d[i]).intersection(my_keyword)
    if max_count < len(val):
      max_count = len(val)
      recommend_region = i

  # 내가 선택한 키워드를 최대로 많이 포함하고 있는 모두 찾기 -> 처음 추천 지역!
  for i in d.keys():
    val = set(d[i]).intersection(my_keyword)
    if max_count == len(val):
      max_count = len(val)
      recommend_regions.append(i)
  return ",".join(recommend_regions)


# user-user collaborative filtering

# name과 다른 사용자와의 유사도 계산하는 함수
def cosim(name, df):
  regions = []  # name이 평가한 지역만 담은 리스트
  for i in df.loc[name, :].index:
    if math.isnan(df.loc[name, i]) == False:
      regions.append(i)

  # name이 평가한 지역만 추출한 데이터프레임
  user_df = pd.DataFrame(df.loc[name, regions]).T

  # name 사용자를 제외한 데이터프레임
  other_df = df.drop([name])

  sim_dict = {}  # name과 다른 사용자 간의 유사도 평가한 결과

  # user와 name 둘 다 평점을 매긴 지역에 대한 벡터로 코사인 유사도 구하기
  for user in other_df.index:
    other_regions = []  # name 제외한 user가 name이 평가한 지역만 담은 리스트
    for i in user_df.columns:
      if math.isnan(other_df.loc[user, i]) == False:
        other_regions.append(i)
    # ** norm은 벡터의 길이 혹은 크기를 측정하는 방법
    # name이 평가한 지역과 user가 평가한 지역의 교집합에 대한 name의 벡터의 길이
    main_n = np.linalg.norm(user_df.loc[name, other_regions])
    # name이 평가한 지역과 user가 평가한 지역의 교집합에 대한 user의 벡터의 길이
    user_n = np.linalg.norm(other_df.loc[user, other_regions])
    prod = np.dot(user_df.loc[name, other_regions], other_df.loc[user, other_regions])

    sim_dict[user] = prod / (main_n * user_n)

  return sim_dict

# name에게 추천할 지역 찾아주는 함수(return : 추천 지역들의 리스트)
# df : 모든 사용자의 별점 데이터
# k : name과 유사도가 큰 k명의 사용자로부터 추천해줌
def find_similar_regions_from_user_user(name, df, k):
  sim_dict = cosim(name, df)

  # name과 다른 사용자와의 유사도 내림차순 정렬
  sim_mat = sorted(sim_dict.items(), key=operator.itemgetter(1), reverse=True)
  print("비슷한 사용자: ", sim_mat)

  regions = []  # name이 평가한 지역만 담은 리스트
  for i in df.loc[name, :].index:
    if math.isnan(df.loc[name, i]) == False:
      regions.append(i)

  # name이 평가한 지역만 추출한 데이터프레임
  user_df = pd.DataFrame(df.loc[name, regions]).T

  # name이 평가하지 않은 영화 추출
  recommend_list = list((set(df.columns) - set(user_df.columns)))

  others_k = [i[0] for i in sim_mat]
  recommender = {}  # key=name이 안 매긴 지역, value=예측 평점

  for region in recommend_list:
    rating = []  # name이 평가하지 않은 지역을 유사도 높은 사용자 순으로 그 사용자가 평가했다면,
    # name이 평가 하지 않은 지역의 그 사용자가 매긴 평점 담음
    sim = []  # name이 평가하지 않은 지역을 유사도 높은 사용자 순으로 그 사용자가 평가했다면,
    # 그 사용자와 name간의 유사도 담음
    for user in others_k:
      if math.isnan(df.loc[user, region]) == False:
        rating.append(df.loc[user, region])
        sim.append(sim_dict[user])

    # k명의 다른 사용자가 매긴 평점으로 예측함
    upper = 0  # 예측(KNN가중치) 분자
    for i in range(k):
      upper += sim[i] * rating[i]
    pred = upper / (sum(sim[:k]))

    # 예상 별점이 3이상인 지역만 추천해줌
    if 3 <= pred:
      recommender[region] = pred

  print(recommender)
  # 예상 평점 내림차순으로 정렬
  sorted(recommender.items(), key=operator.itemgetter(1), reverse=True)

  return ",".join(list(recommender.keys()))
