//package Util;
//
//import java.util.Random;
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;
//
//public class cal24 {
//    private static class Data{
//        public float[]  arr;
//        public String optStr="";
//        public String[] strs;
//        public Data(){}
//        public Data(int a,int b,int c,int d) {
//            arr=new float[]{a,b,c,d};
//            strs=new String[]{a+"",b+"",c+"",d+""};
//            optStr=a+"";
//        }
//
//    }
//    private static void count(Data data){
//        float[] arr=data.arr;
//            if(arr.length<=1){
//            if(arr.length==1&&Math.abs(24-arr[0])<0.00001f){//增加精度判断，修复不包括某些除法算法的bug
//                System.out.println("24! "+data.optStr);
//            }
//            return ;
//        }
//        for(int i=0,len=arr.length;i<len-1; i++){
//            for(int j=i+1;j<len;j++){
//                float x=arr[i];
//                float y=arr[j];
//                String xs=data.strs[i];
//                String ys=data.strs[j];
//                for(int k=0;k<6;k++){
//                    Data newData=getNewArr(data,i);
//                    switch(k){
//                        case 0:
//                            newData.arr[j-1]=x+y;
//                            newData.optStr=xs+"+"+ys;
//                            break;
//                        case 1:
//                            newData.arr[j-1]=x-y;
//                            newData.optStr=xs+"-"+ys;
//                            break;
//                        case 2:
//                            newData.arr[j-1]=y-x;
//                            newData.optStr=ys+"-"+xs;
//                            break;
//                        case 3:
//                            newData.arr[j-1]=x*y;
//                            newData.optStr=xs+"*"+ys;
//                            break;
//                        case 4:
//                            if(y!=0){
//                                newData.arr[j-1]=x/y;
//                                newData.optStr=xs+"/"+ys;
//                            }else {
//                                continue;
//                            }
//                            break;
//                        case 5:
//                            if(x!=0){
//                                newData.arr[j-1]=y/x;
//                                newData.optStr=ys+"/"+xs;
//                            }else {
//                                continue;
//                            }
//                            break;
//                    }
//                    newData.optStr="("+newData.optStr+")";
//                    newData.strs[j-1]=newData.optStr;
//                    count(newData);
//                }
//            }
//        }
//
//    }
//    private static Data getNewArr(Data data, int i) {
//        Data newData=new Data();
//        newData.optStr=data.optStr;
//        newData.arr=new float[data.arr.length-1];
//        newData.strs=new String[data.arr.length-1];
//        for(int m=0,len=data.arr.length,n=0;m<len;m++){
//            if(m!=i){
//                newData.arr[n]=data.arr[m];
//                newData.strs[n]=data.strs[m];
//                n++;
//            }
//        }
//        return newData;
//    }
//    private static void generateNum(){
//        double randDouble=new Random().nextDouble();
//        while (randDouble==0d) {
//            System.out.println(randDouble);
//            randDouble=new Random().nextDouble();
//        }
//    }
//    public static void main(String[] args) throws InterruptedException {
//        /*long start=System.currentTimeMillis();
//        Data d=new Data(1,2,3,4);
//        count(d);
//
//        System.out.println(System.currentTimeMillis()-start);*/
//        /*Random random=new Random();
//        for (int i = 0; i < 10; i++) {
//            System.out.println(random.nextDouble());
//        }*/
//
////        Matcher matcher=Pattern.compile("^((?:(\\d+\\)+)|\\d+|(\\(+\\d+))[\\+|\\-|\\*|/])+(?:(\\d+\\)+)|\\d+)$")
////            .matcher("(9-1)*(10-7)");
////        System.out.println(matcher.find());
//        long start=System.currentTimeMillis();
//        Data data=new Data(3, 3, 8, 8);
//        count(data);
//        System.out.println(data.optStr);
//        System.out.println(System.currentTimeMillis()-start);
//    }
//}