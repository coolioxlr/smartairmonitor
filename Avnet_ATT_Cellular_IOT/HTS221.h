/* ===================================================================
Copyright © 2016, AVNET Inc.  

Licensed under the Apache License, Version 2.0 (the "License"); 
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, 
software distributed under the License is distributed on an 
"AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, 
either express or implied. See the License for the specific 
language governing permissions and limitations under the License.

======================================================================== */

#ifndef HTS221_H_
#define HTS221_H_

class HTS221 {
public:
    HTS221(void);
    int begin(void);
    int activate(void);
    int deactivate(void);

    int bduActivate(void);
    int bduDeactivate(void);

    int readHumidity(void);
    double readTemperature(void);
private:
    int storeCalibration(void);
    unsigned char _h0_rH, _h1_rH;
    unsigned int  _T0_degC, _T1_degC;
    unsigned int  _H0_T0, _H1_T0;
    unsigned int  _T0_OUT, _T1_OUT;
    double _temperature;
    int _humidity;
    unsigned char _address;

    unsigned char readRegister(unsigned char slaveAddress, unsigned char regToRead);
    int writeRegister(unsigned char slaveAddress, unsigned char regToWrite, unsigned char dataToWrite);
};

#define HTS221_ADDRESS     0xBF

//Define a few of the registers that we will be accessing on the HTS221
#define WHO_AM_I           0x0F
#define WHO_AM_I_RETURN    0xBC //This read-only register contains the device identifier, set to BCh

#define AVERAGE_REG        0x10 // To configure humidity/temperature average.
#define AVERAGE_DEFAULT    0x1B

/*
 * [7] PD: power down control
 * (0: power-down mode; 1: active mode)
 *
 * [6:3] Reserved
 *
 * [2] BDU: block data update
 * (0: continuous update; 1: output registers not updated until MSB and LSB reading)
The BDU bit is used to inhibit the output register update between the reading of the upper
and lower register parts. In default mode (BDU = ?0?), the lower and upper register parts are
updated continuously. If it is not certain whether the read will be faster than output data rate,
it is recommended to set the BDU bit to ?1?. In this way, after the reading of the lower (upper)
register part, the content of that output register is not updated until the upper (lower) part is
read also.
 *
 * [1:0] ODR1, ODR0: output data rate selection (see table 17)
 */
#define CTRL_REG1          0x20
#define POWER_UP           0x80
#define BDU_SET            0x4
#define ODR0_SET           0x1   // setting sensor reading period 1Hz

#define CTRL_REG2          0x21
#define CTRL_REG3          0x22
#define REG_DEFAULT        0x00

#define STATUS_REG         0x27
#define TEMPERATURE_READY  0x1
#define HUMIDITY_READY     0x2

#define HUMIDITY_L_REG     0x28
#define HUMIDITY_H_REG     0x29
#define TEMP_L_REG         0x2A
#define TEMP_H_REG         0x2B
/*
 * calibration registry should be read for temperature and humidity calculation.
 * Before the first calculation of temperature and humidity,
 * the master reads out the calibration coefficients.
 * will do at init phase
 */
#define CALIB_START        0x30
#define CALIB_END          0x3F
/*
#define CALIB_T0_DEGC_X8   0x32
#define CALIB_T1_DEGC_X8   0x33
#define CALIB_T1_T0_MSB    0x35
#define CALIB_T0_OUT_L     0x3C
#define CALIB_T0_OUT_H     0x3D
#define CALIB_T1_OUT_L     0x3E
#define CALIB_T1_OUT_H     0x3F
 */
 
#endif
