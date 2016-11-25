package com.eegsmart.changcolor.utils;

import android.os.Environment;
import android.util.Log;

import com.eegsmart.changcolor.ChangColor;
import com.eegsmart.changcolor.config.MyConstans;
import com.eegsmart.changcolor.enity.FFTData;
import com.eegsmart.changcolor.enity.Histogram;
import com.eegsmart.changcolor.view.LineView;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;

/**
 * Created by User on 2016/5/13.
 */
public class SaveFFTData {
    private static final String TAG = SaveFFTData.class.getSimpleName();

    private static final double N1 = 3.0;
    private static final double N2 = 2.0;
    private static final double N3 = 1.0;
    private static final double WAKE = 5.0;
    private static final double REM = 4.0;


    private static SaveFFTData instance = new SaveFFTData();
    private FileWriter fwSleepData = null;
    private FileWriter fwRemdata = null;
    private FileWriter fwNoiseData = null;

    private boolean canWrite = true;

    public ArrayList<Double> sleepList = new ArrayList<>();
    public ArrayList<Integer> remList = new ArrayList<>();
    public File fileSleep;
    public int[] finalRemArr;
    public double[] newSleepArr;
    private ArrayList<Histogram> drawDataList = new ArrayList<>();
    public int[] transData;

    public static SaveFFTData getInstance() {
        return instance;
    }

    private static DateFormat format;

    private SaveFFTData() {
        createFiles();
    }

    public void saveFFTData(ArrayList<FFTData> list) {
        long start = System.currentTimeMillis();
        ArrayList<Double> elList = new ArrayList<>();
        ArrayList<Double> ehList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).el != 0d) {
                elList.add(list.get(i).el);
            }
            if (list.get(i).eh != 0d) {
                ehList.add(list.get(i).eh);
            }
        }
        double[] el = new double[elList.size()];
        double[] eh = new double[ehList.size()];
        for (int i = 0; i < elList.size(); i++) {
            el[i] = elList.get(i);
        }
        for (int i = 0; i < ehList.size(); i++) {
            eh[i] = ehList.get(i);
        }
        double avgEl = oStu(el);
        double avgEh = oStu(eh);

        int i = 0;
        double[] sleep = new double[list.size()];
        int[] eyemovement = new int[list.size()];
        int[] noise = new int[list.size()];
        int eyecount = 0;
        for (FFTData fftData : list) {
            if (fftData.el > avgEl && fftData.eh < avgEh) {
                //满足眼动判断
                fftData.eyeMovement = 1;
                fftData.noise = 0;
                eyecount++;
            } else if (fftData.sleeping == 0) {
                //不满足，数据污染
                fftData.noise = 1;
                fftData.eyeMovement = 0;
            }

            sleep[i] = fftData.sleeping;
            noise[i] = fftData.noise;
            eyemovement[i] = fftData.eyeMovement;
            i++;
        }

        int sCount = 0;
        for (double s : sleep) {
            if (s != 0d) {
                sCount++;
            }
        }
        for (int e : eyemovement) {
            Log.e(TAG, "eye:" + e);
        }
        int nCount = 0;
        for (int n : noise) {
            if (n == 1) {
                nCount++;
            }
        }
        Log.e(TAG, "eyecount :" + eyecount + " nCount : " + nCount);
//        for (int no : noise) {
//            try {
//                fwNoiseData.write(no + "\t\n");
//                fwNoiseData.flush();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        for (int z = 0; z < sleep.length; z++) {
//            try {
//                if (z < eyemovement.length) {
//                    fwRemdata.write(eyemovement[z] + "\t\n");
//                    fwRemdata.flush();
//                }
//                fwSleepData.write(sleep[z] + "\t\n");
//                fwSleepData.flush();
//            } catch (IOException ioe) {
//                ioe.printStackTrace();
//                canWrite = false;
//            }
//        }

        int[] eye = Utils.transe11To10(eyemovement);
        double sleepSum = 0;
        int sleepCount = 0;
        int noiseCount = 0;
        int eyeCount = 0;
        int rem = 0;
        double sleeping = 0;
        double abs = 0;
        for (int j = 0; j < sleep.length; j++) {
            if (sleep[j] != 0) {
                sleepSum += sleep[j];
                //不为零的sleep值个数
                sleepCount++;
            }
            if (noise[j] == 1) {
                noiseCount++;
            }
            if ((j + 1) % 30 == 0) {
                //30秒的sleep均值计算
                if (noiseCount <= 3) {
                    //noise 不超过3个
                    if (sleepCount != 0) {
                        sleeping = sleepSum / sleepCount;
//                        abs = sleeping;
                        abs = sleeping + 1;
                        Log.e(TAG, "sleeping :" + abs);
                        sleepList.add(abs);
                    }
                } else {
                    //用上一个周期的sleeping填充
                    sleepList.add(abs);
                }
                //30秒后 sum值 个数重新置0
                sleepSum = 0;
                sleepCount = 0;
                noiseCount = 0;
            }

            if (eye[j] == 1 && j > 29) {
                //统计eye 1 的个数
                eyeCount++;
            }
            //第一个sleep周期（30s）不用判断 直接赋予rem 0
            if ((j + 1) % 30 == 0) {
                if (j == 29) {
                    rem = 0;
                    remList.add(rem);
                } else {
                    remList.add(eyeCount);
                    //30秒后重置个数
                    eyeCount = 0;
                }
            }
        }
//
        int rCount = 0;
        for (int x = 0; x < remList.size(); x++) {
            if (remList.get(x) != 1) {
                rCount++;
            }
            Log.e(TAG, "rem:" + remList.get(x));
        }
        Log.e(TAG, "rCount : " + rCount);
        newSleepArr = getSleepWeightedAvg(sleepList);
        finalRemArr = getRemWeightedAvg(remList);
        for (int z = 0; z < newSleepArr.length; z++) {
            Log.e(TAG, "sleepList : " + newSleepArr.length);
            try {
                if (z < finalRemArr.length) {
                    fwRemdata.write(finalRemArr[z] + "\t\n");
                    fwRemdata.flush();
                }
                fwSleepData.write(newSleepArr[z] + "\t\n");
                fwSleepData.flush();
            } catch (IOException ioe) {
                ioe.printStackTrace();
                canWrite = false;
            }
        }
        transData = getTransData(finalRemArr, newSleepArr);


//        for (int z = 0; z < transData.length; z++) {
//            try {
//                if (z < finalRemArr.length) {
//                    fwRemdata.write(finalRemArr[z] + "\t\n");
//                    fwRemdata.flush();
//                }
//                fwSleepData.write(transData[z] + "\t\n");
//                fwSleepData.flush();
//            } catch (IOException ioe) {
//                ioe.printStackTrace();
//                canWrite = false;
//            }
//        }


    }

    /**
     * 对Rem值进行加权平均后，判断区间，转化为0，1值
     *
     * @param remList
     * @return
     */

    private int[] getRemWeightedAvg(ArrayList<Integer> remList) {
        double[] newRemArr = new double[remList.size()];
        if (remList.size() > 120) {
            for (int z = 3; z < remList.size() - 3; z++) {
                double value = 0.05 * remList.get(z - 3) + 0.1 * remList.get(z - 2) + 0.2 * remList.get(z - 1) + 0.3 * remList.get(z) + 0.2 * remList.get(z + 1) + 0.1 * remList.get(z + 2) + 0.05 * remList.get(z + 3);
                newRemArr[z] = value;
                if (z == 6) {
                    double v = newRemArr[z - 3];
                    newRemArr[0] = v;
                    newRemArr[1] = v;
                    newRemArr[2] = v;
                }
                if (z == remList.size() - 3 - 1) {
                    newRemArr[z + 1] = value;
                    newRemArr[z + 2] = value;
                    newRemArr[z + 3] = value;
                }
            }
            int[] finalRemArr = new int[newRemArr.length];
            //加权平均后再次判断rem
            for (int y = 0; y < newRemArr.length; y++) {
                double data = newRemArr[y];
                if (data >= MyConstans.EYECOUNTMIN && data <= MyConstans.EYECOUNTMAX) {
                    finalRemArr[y] = 1;
                } else {
                    finalRemArr[y] = 0;
                }
            }
            return finalRemArr;
        } else {
            int[] finalRemArr = new int[remList.size()];
            for (int y = 0; y < remList.size(); y++) {
                double data = remList.get(y);
                if (data >= 3 && data <= 10) {
                    finalRemArr[y] = 1;
                } else {
                    finalRemArr[y] = 0;
                }
            }
            return finalRemArr;
        }
    }

    /**
     * 对sleep值进行加权平均
     *
     * @param sleepList
     * @return
     */

    private double[] getSleepWeightedAvg(ArrayList<Double> sleepList) {
        double[] newSleepArr = new double[sleepList.size()];
        if (sleepList.size() > 120) {
            for (int z = 3; z < sleepList.size() - 3; z++) {
                double value = 0.05 * sleepList.get(z - 3) + 0.1 * sleepList.get(z - 2) + 0.2 * sleepList.get(z - 1) + 0.3 * sleepList.get(z) + 0.2 * sleepList.get(z + 1) + 0.1 * sleepList.get(z + 2) + 0.05 * sleepList.get(z + 3);
                newSleepArr[z] = value;
                if (z == 6) {
                    double v = newSleepArr[z - 3];
                    newSleepArr[0] = v;
                    newSleepArr[1] = v;
                    newSleepArr[2] = v;
                }
                if (z == sleepList.size() - 3 - 1) {
                    newSleepArr[z + 1] = value;
                    newSleepArr[z + 2] = value;
                    newSleepArr[z + 3] = value;
                }
            }
            return newSleepArr;
        } else {
            for (int z = 0; z < sleepList.size(); z++) {
                newSleepArr[z] = sleepList.get(z);
            }
            return newSleepArr;
        }
    }

    /**
     * rem 与 sleep的叠加 ，sleep值经过判断转化为1 - 5 值
     *
     * @param
     * @return
     */
    private int[] getTransData(int[] remArr, double[] sleepArr) {
        int[] histogramArr = new int[sleepArr.length];
        for (int x = 0; x < remArr.length; x++) {
            if (x < 30) {
                //前30个 置0
                remArr[x] = 0;
            }
            if (x > remArr.length - 15) {
                //后15个 置0
                remArr[x] = 0;
            }
            int remdata = remArr[x];
            double newSleep = sleepArr[x];
            newSleep = remdata + newSleep;
            sleepArr[x] = newSleep ;
            Double aDouble = sleepArr[x];
            if (x < 10) {
                histogramArr[x] = MyConstans.N1;
            } else if (x < 30) {
                histogramArr[x] = MyConstans.N2;
            } else {
                if (aDouble < MyConstans.N3_JUDGE) {
                    histogramArr[x] = MyConstans.N3;
                } else if (aDouble < MyConstans.N2_JUDGE) {
                    histogramArr[x] = MyConstans.N2;
                } else if (aDouble < MyConstans.N1_JUDGE) {
                    histogramArr[x] = MyConstans.N1;
                } else if (aDouble < MyConstans.REM_JUDGE) {
                    histogramArr[x] = MyConstans.WAKE;
                } else if (aDouble > MyConstans.REM_JUDGE) {
                    histogramArr[x] = MyConstans.REM;
                }
            }
        }
        return histogramArr;
    }

    /**
     * 3个判断一次睡眠状态，6个判断一次rem状态，rem的判断会把之前的覆盖
     *
     * @param histogramArr
     * @return
     */
    private ArrayList<Histogram> getDrawData(int[] histogramArr) {
        int n1Conut = 0;
        int n2Conut = 0;
        int n3Conut = 0;
        int n4Conut = 0;
        int remConut = 0;
        int sumCount = histogramArr.length / 3;
        int type = MyConstans.N1;
        for (int i = 0; i < histogramArr.length; i++) {
            int size = histogramArr.length;
            int ty = histogramArr[i];
            switch (ty) {
                case MyConstans.N1:
                    n1Conut++;
                    break;
                case MyConstans.N2:
                    n2Conut++;
                    break;
                case MyConstans.N3:
                    n3Conut++;
                    break;
                case MyConstans.WAKE:
                    n4Conut++;
                    break;
                case MyConstans.REM:
                    remConut++;
                    break;
            }
            //3个（1.5分钟）判断一次
            if ((i + 1) % 3 == 0) {
                int[] arr = new int[]{n1Conut, n2Conut, n3Conut, n4Conut};
                int max = Utils.getTypeMax(arr);
                if (max == n1Conut) {
                    type = MyConstans.N1;
                } else if (max == n2Conut) {
                    type = MyConstans.N2;
                } else if (max == n3Conut) {
                    type = MyConstans.N3;
                } else if (max == n4Conut) {
                    type = MyConstans.WAKE;
                } else if (max == -1) {
                    //用上一个周期的type
                }

                drawDataList.add(new Histogram(type, 1.0f / sumCount));
                //判断完成后 计数重新置0
                n1Conut = 0;
                n2Conut = 0;
                n3Conut = 0;
                n4Conut = 0;
            }
            if (drawDataList.size() != 0 && drawDataList.size() % 2 == 0) {
                if (remConut >= 2) {
                    //记录这三分钟为rem
                    int index = drawDataList.size();
                    drawDataList.set(index - 2, new Histogram(MyConstans.REM, 1.0f / sumCount));
                    drawDataList.set(index - 1, new Histogram(MyConstans.REM, 1.0f / sumCount));
                }
                remConut = 0;
            }
        }
        return drawDataList;
    }

    private void createFiles() {
        File SDCard = Environment.getExternalStorageDirectory();
        format = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        File fileDir = new File(SDCard.getAbsolutePath() + File.separator + "ChangColor" + File.separator + "FFTData");
        String currentTime = format.format(new Date(System.currentTimeMillis()));
        if (!fileDir.exists()) {
            fileDir.mkdirs();
        }
//        File fileFFT = new File(fileDir.getAbsolutePath() + File.separator + "fftdata-" + currentTime + ".txt");
//        File fileRaw = new File(fileDir.getAbsolutePath() + File.separator + "rawdata-" + currentTime + ".txt");
//        File fileSleep = new File(fileDir.getAbsolutePath() + File.separator + "原始的sleepdata7-" + currentTime + ".txt");
//        File fileRem = new File(fileDir.getAbsolutePath() + File.separator + "原始的eye7-" + currentTime + ".txt");
//        File fileNoise = new File(fileDir.getAbsolutePath() + File.separator + "原始的noisedata7-" + currentTime + ".txt");
//        File fileSleep = new File(fileDir.getAbsolutePath() + File.separator + "30秒处理后的sleepdata7-" + currentTime + ".txt");
//        File fileRem = new File(fileDir.getAbsolutePath() + File.separator + "原始的eye7-" + currentTime + ".txt");
//        File fileRem = new File(fileDir.getAbsolutePath() + File.separator + "原始的rem7-" + currentTime + ".txt");
        File fileSleep = new File(fileDir.getAbsolutePath() + File.separator + "加权平均后的sleepdata7-" + currentTime + ".txt");
        File fileRem = new File(fileDir.getAbsolutePath() + File.separator + "加权平均后转0，1的rem7-" + currentTime + ".txt");
//        File fileSleep = new File(fileDir.getAbsolutePath() + File.separator + "加权平均前的rem7-" + currentTime + ".txt");
//        fileSleep = new File(fileDir.getAbsolutePath() + File.separator + "加权平均后转柱状图(+rem)的sleepdata7-" + currentTime + ".txt");
        ChangColor.getInstance().setSaveFile(fileSleep);
        try {
//            fwNoiseData = new FileWriter(fileNoise);
//            fwNoiseData.flush();
            fwRemdata = new FileWriter(fileRem);
            fwRemdata.flush();
            fwSleepData = new FileWriter(fileSleep);
            fwSleepData.flush();
        } catch (Exception e) {
            canWrite = false;
        }
    }

    private int bytes2Integer(byte b1, byte b2) {
        int a = byte2Int(b1);
        a = a << 8;
        a = a + byte2Int(b2);
        return a;
    }

    private int byte2Int(byte b) {
        return 0xff & b;
    }

    public double oStu(double[] data) {

        //def ostu(data):
        //#print data
        int length = data.length;
        //length=len(data)
        //if length == 0:
        //return '数组为空' #返回0代表输入数据长度为0
        if (length == 0)
            return 0;

        //bestThreshold=0.0
        //maxvariance=0.0
        double bestThreshold = 0.0;
        double maxvariance = 0.0;

        for (int i = 0; i < 100; i++) {
            double tempvar = 0;
            double SumLow = 0;
            double NumLow = 0.01;
            double SumHigh = 0;
            double NumHigh = 0.01;
            double threshold = i / 100.0 + 0.01;

            for (int j = 0; j < length; j++) {
                //#print j
                if (data[j] <= threshold) {
                    SumLow += data[j];
                    NumLow += 1;
                } else {
                    SumHigh += data[j];
                    NumHigh += 1;
                }
                tempvar = ((NumLow * NumHigh) / Math.pow(length, 2)) * Math.pow((SumLow / NumLow - SumHigh / NumHigh), 2);//(SumLow/NumLow-SumHigh/NumHigh)**2;

                if (maxvariance < tempvar) {
                    maxvariance = tempvar;
                    bestThreshold = threshold;
                }
            }
        }
        return bestThreshold;
    }

}
