package com.gzonesoft.domain;

public class DispatchDisplayDto {
/*

-- EAI 파트
{
  "OIGNRULE": "901",
  "VBELN": "305527203",
  "KUNAG": "1021511",
  "KUNWE_NM": "늘푸른주유소",
  "VEH_TEXT": "인천85사9999",
  "TRIP": "1",
  "ARKTX": "ULSD",
  "SCHED_STTM": "00:00:00",
  "WERKS": "2120",
  "LFART": "LF",
  "VSBED_NM": "TT-용차-HDO수송",
  "CARRIER": "0003000065",
  "ZSEALYN": "N",
  "SCH_QTY": "4000",
  "LFART_NM": "판매 납품",
  "ZZVECODE": "0003000408",
  "SAM_FLAG": "",
  "MATNR": "10270",
  "SCHED_STDT": "2020-02-13",
  "ZZVECODE_TEXT": "하나에너지주식회사",
  "VEHICLE": "85-9999",
  "VSBED": "1H",
  "PUR_FLAG": "",
  "KUNAG_NM": "늘푸른주유소",
  "SHNUMBER": "8802693",
  "POSNV": "10",
  "SCH_UOM": "L",
  "ZGNTYPE": "G",
  "POSNN": "10",
  "VBELV": "206667600",
  "KUNWE": "1021511",
  "CARRIER_NM": "현대글로비스 주식회사",
  "WERKS_NM": "성남저유소"
}
* */
    // EAI 파트
    public String LOADING_REQUEST_ID;   //SEQ_LOADING_REQUEST 로 생성
    public String SHNUMBER;   //TD 선적 번호  PK
    public String VBELN;   //납품문서번호  PK
    public String POSNN;   //납품문서품목  PK
    public String VBELV;   //주문문서번호
    public String POSNV;   //주문문서품목
    public String KUNAG;   //판매처(고객 코드)
    public String KUNAG_NM;   //판매처(고객 명)
    public String KUNWE;   //인도처(고객 코드)
    public String KUNWE_NM;   //인도처(고객 명)
    public String WERKS;   //출하처(플랜트 Code)
    public String WERKS_NM;   //출하처(플랜트명)
    public String LFART;   //납품유형
    public String LFART_NM;   //납품유형명
    public String MATNR;   //제품코드
    public String ARKTX;   //제품명
    public String SCH_QTY;   //배차수량
    public String SCH_UOM;   //배차단위  납품 문서 단위 사용(?)
    public String OIGNRULE;   //총/순 가격규칙  901902903904
    public String ZGNTYPE;   //출하방식(G/N)
    public String SCHED_STDT;   //선적 계획일자
    public String SCHED_STTM;   //선적 계획시간
    public String VEHICLE;   //TD 차량번호
    public String VEH_TEXT;   //TD 차량이름
    public String CARRIER;   //운송업체코드
    public String CARRIER_NM;   //운송업체명
    public String ZZVECODE;   //실행사코드
    public String ZZVECODE_TEXT;   //실행사명
    public String TRIP;   //트립  배차 회수
    public String VSBED;   //운송모드  출하조건
    public String VSBED_NM;   //출하조건명
    public String PUR_FLAG;   //식별제 주입대상
    public String SAM_FLAG;   //시료채취여부
    public String ZSEALYN;   //봉인 Key 입력여부  Y:입력,N:미입력
    public String SHV_SEQ;   //회차번호(일단위차량당 선적차수)
    
}
