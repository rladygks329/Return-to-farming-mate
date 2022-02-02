import socket
import getRegionKeyword
import recommendRegion
import pandas as pd
import json

url = "https://api.odcloud.kr/api/15013093/v1/uddi:e6879cfc-fc35-436e-8e33-f7ca204a91dd_202004091343?page=1&perPage=463&serviceKey=86Tx3s6cooxzUiEx6enuppS3KPCwasgBurkXdOnDtsMPIpB0Ly2u2%2Fbsj7CUNirerxgLRIhs3JffqoyHkhFGPQ%3D%3D"
host = "172.19.112.1"  # 호스트 ip
port = 9090
# data = getRegionKeyword.getKeyword(url)
server_sock = socket.socket(socket.AF_INET)
server_sock.bind((host, port))
server_sock.listen(1)
print("기다리는 중...")
flag = 0

while True:
  client_sock, addr = server_sock.accept()

  if client_sock:
    print("-----")
    gubun = client_sock.recv(1024)[2:].decode("utf-8")
    print("gubun: ", gubun)

    # 키워드 전송함 <ChooseKeyword.class>
    if gubun == "0":
      # 1. api에서 키워드 추출해서 안드로이드로 보냄
      # client_sock.send(data.encode("utf-8"))
      tmp = 0

    # 안드로이드에서 사용자가 선택한 키워드 받고, 추천할 지역 뽑아서  안드로이드로 보냄 (키워드로 추천)-cold start 문제 해결
    # <ChooseKeyword.class>
    elif gubun == "1":
      # 1. 안드로이드에서 사용자가 선택한 키워드 받음
      my_keyword = client_sock.recv(1024)[2:].decode("utf-8")
      print("keyword: ", my_keyword)

      # 2. 받은 키워드로 추천할 지역 뽑아서 안드로이드로 보냄
      if my_keyword:
        recommend_regions = recommendRegion.find_recommend_region_from_keyword(my_keyword, url)

      if recommend_regions:
        client_sock.send(recommend_regions.encode("utf-8"))
        print("regions: ", recommend_regions)

    # user-user collaborating으로 추천 지역 안드로이드로 보냄
    elif gubun == "2":
      # 1. 안드로이드로부터 사용자의 이메일, 별점 데이터 받음
      ratings = client_sock.recv(1024)[2:].decode("utf-8")
      email = client_sock.recv(1024)[2:].decode("utf-8")

      print("email: ", email)
      print("ratings: ", ratings)
      # email = email[2:]
      # print("email: ", email)
      # 2. user-user collaborating으로 추천 지역 뽑음
      if ratings and email:
        # string으로 받은 ratings를 dictionary로 형변환 함
        string_to_dict = json.loads(ratings.replace("'", "\""))
        df_ratings = pd.DataFrame(string_to_dict).T
        print("ratings: ", df_ratings)
        recommend_regions_from_users = recommendRegion.find_similar_regions_from_user_user(email, df_ratings, 1)
        print("recommend: ", recommend_regions_from_users)
        # 3. 안드로이드에게 추천 지역 보내기
        if recommend_regions_from_users:
          print("보냄!!!")
          client_sock.send(recommend_regions_from_users.encode("utf-8"))

    else:
      print("gubun 안옴")

client_sock.close()
server_sock.close()


