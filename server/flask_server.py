from flask import Flask
from flask import request
import getRegionKeyword
import time

from returnfarming import recommendRegion

app = Flask(__name__)
url = "https://api.odcloud.kr/api/15013093/v1/uddi:e6879cfc-fc35-436e-8e33-f7ca204a91dd_202004091343?page=1&perPage=463&serviceKey=86Tx3s6cooxzUiEx6enuppS3KPCwasgBurkXdOnDtsMPIpB0Ly2u2%2Fbsj7CUNirerxgLRIhs3JffqoyHkhFGPQ%3D%3D"

# open api에서 추출한 키워드 리턴
@app.route("/get-keywords")
def retrieveKeywords():
    keywords = "농촌,공동체,체험,행복,전통,꽃,미래, 세계"
    return keywords
#@app.route("/get-keywords")
# def retrieveKeywords():
#     start = time.time()
#     keywords = getRegionKeyword.getKeyword(url)
#     print("time: ", time.time()-start)
#     return keywords


# 선택된 키워드 받아서 추천 지역 리턴
@app.route("/get-regions-from-keyword")
def getRegionsFromKeyword():
    selectedKeywords = request.args.get('selectedKeywords', "")
    recommend_regions = ""
    if 1 < len(selectedKeywords):
        recommend_regions = recommendRegion.find_recommend_region_from_keyword(selectedKeywords, url)
    return recommend_regions

if __name__ == '__main__':
    app.run(debug=True)
