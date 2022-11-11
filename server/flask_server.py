from flask import Flask
from flask import request
import getRegionKeyword
import json
import pandas as pd
from returnfarming import recommendRegion
import firebase_admin
from firebase_admin import credentials
from firebase_admin import firestore

app = Flask(__name__)
url = "https://api.odcloud.kr/api/15013093/v1/uddi:e6879cfc-fc35-436e-8e33-f7ca204a91dd_202004091343?page=1&perPage=463&serviceKey=86Tx3s6cooxzUiEx6enuppS3KPCwasgBurkXdOnDtsMPIpB0Ly2u2%2Fbsj7CUNirerxgLRIhs3JffqoyHkhFGPQ%3D%3D"
cred = credentials.Certificate('./')
firebase_admin.initialize_app(cred)
db = firestore.client()

# open api에서 추출한 키워드 리턴
@app.route("/get-keywords")
def retrieveKeywords():
    keywords = getRegionKeyword.getKeyword(url)
    response = {'body': keywords}
    return json.dumps(response)


# 선택된 키워드 받아서 추천 지역 리턴
@app.route("/get-regions-from-keyword", methods=['POST'])
def getRegionsFromKeyword():
    data = request.get_json()
    email = data['email']
    selectedKeywords = data['body']
    recommend_regions = ""
    # print(selectedKeywords)
    if 1 < len(selectedKeywords):
        recommend_regions = recommendRegion.find_recommend_region_from_keyword(selectedKeywords, url)
    # print(recommend_regions)

    # db에 사용자 기반 추천 지역 저장
    user_ref = db.collection(u'users').document(email)
    lst_regions = recommend_regions.split(",")
    user_ref.set({u'recommendRegions': lst_regions},
                 merge=True)  # recommendRegions 값 덮어쓰기

    return "success"

# rating 데이터 기반으로 사용자 기반 추천 지역 리턴
@app.route("/get-regions-from-user", methods=['POST'])
def getRegionsFromUser():
    data = request.get_json()
    email = data['email']
    ratings = data['body']

    # print(email, ratings)
    if ratings and email:
        # string으로 받은 ratings를 dictionary로 형변환 함
        string_to_dict = json.loads(ratings.replace("'", "\""))
        df_ratings = pd.DataFrame(string_to_dict).T
        recommend_regions_from_users = recommendRegion.find_similar_regions_from_user_user(email, df_ratings, 1)
        # print("recommend: ", recommend_regions_from_users)

        # db에 사용자 기반 추천 지역 저장
        user_ref = db.collection(u'users').document(email)
        lst_regions = recommend_regions_from_users.split(",")
        for region in lst_regions:
            user_ref.update({u'recommendRegionsBasedUser': firestore.ArrayUnion([region])})
    return "success"

if __name__ == '__main__':
    app.run(debug=True)
