from urllib.request import urlopen
import json
from collections import Counter
try:
  import jpype
  import jpype1
except:
  import jpype
from konlpy.tag import Okt

def getKeyword(url):
  # keword 뽑아내기 - 빈도수 계산
  responseBody = urlopen(url).read().decode('utf-8')
  jsonArray = json.loads(responseBody)
  dataArray = jsonArray.get("data")

  okt = Okt()
  c = Counter()

  for each in dataArray:
    s = each.get("우수사업명")
    n = okt.nouns(s)
    c.update(n)

  except_lst = ['마을', '우수', '사례', '산업', '문화', '지역', '사업', '차', '지원',
                '농협', '위', '주민', '제주', '역', '자연', '사람', '개발']
  keyword_str = ""  # open api로부터 뽑은 키워드 -> 안드로이드로 전송
  for k, v in c.most_common(25):
    if k not in except_lst:
      keyword_str += k + ","

  keyword_str = keyword_str[:len(keyword_str) - 1]
  return keyword_str