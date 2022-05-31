#include <WiFiEsp.h>
#include <WiFiEspClient.h>
#include <WiFiEspServer.h>
#include <WiFiEspUdp.h>
#include <MySQL_Connection.h>
#include <MySQL_Cursor.h>
#include <SoftwareSerial.h>
#include <Wire.h>
#include "MAX30100_PulseOximeter.h"
#include "HX711.h"

#define REPORTING_PERIOD_MS     1000

// WiFi card example
char ssid[] = "401 2.4G";         // your SSID
char pass[] = "";     // your SSID Password
SoftwareSerial mySerial(9,10); //TX,RX
WiFiEspClient client;
IPAddress server_addr(192,168,0,28); // 서버IP

//산소포화도, bpm
// Create a PulseOximeter object
PulseOximeter pox;
uint32_t tsLastReport = 0;
boolean tmpoxa = true;

//int i =1;
int tmpox = 0;
int count =1;

//저울 변수
float loadcellValue = 372.0;
int now = 0;
int output = 0;
int countScale = 0;
int tmp = 0;

// HX711 circuit wiring
#define LOADCELL_DOUT_PIN         2
#define LOADCELL_SCK_PIN          3
HX711 scale;

 //산소포화도 아두이노
void onBeatDetected() {
    Serial.println("Beat!");
}

void setup() {
  //버튼
  pinMode(12, INPUT_PULLUP);
  
  //시리얼 연결
  Serial.begin(9600);
  mySerial.begin(9600);

  scale.begin(LOADCELL_DOUT_PIN, LOADCELL_SCK_PIN);
  //부팅 후 잠시 대기 (2초)
//  delay(2000);
  // 스케일 설정
  scale.set_scale(loadcellValue);
  scale.tare(10);   
  //측정값 1회 읽어오기
  Serial.print("read: \t\t\t");
  Serial.println(scale.read());
    // 설정된 오프셋 및 스케일 값 확인
  Serial.print("Offset value :\t\t");
  Serial.println(scale.get_offset());
  Serial.print("Scale value :\t\t");
  Serial.println(scale.get_scale());

  // (read - offset) 값 확인 (scale 미적용)
  Serial.print("(read - offset) value: \t"); 
  now = scale.get_value(); 
  Serial.println(now);
  delay(2000);

//  //산소포화도 연결
  Serial.print("산소포화도 연결");
    // Initialize sensor
  if (!pox.begin()) {
    Serial.println("FAILED");
    for(;;);
  } else {
    Serial.println("SUCCESS");
  }
    // Configure sensor to use 7.6mA for LED drive
  pox.setIRLedCurrent(MAX30100_LED_CURR_7_6MA);
//   Register a callback routine
  pox.setOnBeatDetectedCallback(onBeatDetected);  


  
  //와이파이 연결
  WiFi.init(&mySerial);
  Wifi_connect();
} 

void loop() {
int readValue = digitalRead(12);

  if(WiFi.status() != WL_CONNECTED){
    Wifi_connect();
    Serial.println("WIFI연결안됨");
    delay(500);
   }else{
}


  if (readValue == LOW){
    ox();
//    getscale();

  }else{
    getscale();
//ox();
  }

  
}


//서버에 데이터 값 넘김
void InsertData(int water, int ox){
   if (client.connect(server_addr, 8080)) {
    Serial.println("서버접속 성공");
    client.print("GET /members/test?loginid="); 
    client.print("aa"); 
    client.print("&water="); 
    client.print(water); 
    client.print("&ox="); 
    client.println(ox);

    client.flush();
    client.stop();
//    delay(1000);
    Serial.println("데이터 전송 성공");
 }else{
      Serial.println("서버접속 실패");
 }
}

//와이파이 접속체크
void Wifi_connect() {
  Serial.println("---------------------------------------"); 
  Serial.println(ssid); 
  WiFi.begin(ssid, pass); // 와이파이 이름과 비밀번호를 통해 WIFI연결을 시작 // WL_CONNECTED라는 값을 돌려준다 
  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.print("1");
  }
    Serial.println(); 
    Serial.println("Wifi connected!"); 
    Serial.println("\nConnected to network"); 
    Serial.print("My IP address is: "); 
    Serial.println(WiFi.localIP()); 
    delay(500); 
 }


void ox(){
  // Read from the sensor
  int oxdata = 0;
  int oxtmp;
  pox.update();  
    if (millis() - tsLastReport > REPORTING_PERIOD_MS) {
        Serial.print("SpO2:");
        Serial.print(pox.getSpO2());
        Serial.println("%");
        tsLastReport = millis();
//        oxdata = pox.getSpO2();
    }

  if(oxdata > 80 && oxdata < 100 && tmpoxa == true){
    if(pox.getSpO2() != oxtmp){
      InsertData(0, oxdata);
      tmpoxa = false;
      oxtmp = oxdata;      
    }
  }

}

void getscale(){
    now = scale.get_units(10);
  if(count == 5){
    count = 0;
  }
  if(now == tmp || now+1 == tmp || now-1 == tmp){
    count++;
    if(count == 5){
      if(output > now && now > 10 ){
        output -= now;
        if( output > 5){
          InsertData(output, 0);
        }
      }
      if(now !=0){
        output = now;
      }
      if(now < -30){
          scale.tare(10);    
      }
    }
  }else{
    count = 0;
  }
  tmp = now;

  Serial.print("now = ");
  Serial.print(now);
  Serial.print(" count = ");
  Serial.print(count);
  Serial.print(" output = ");
  Serial.println(output);
}
