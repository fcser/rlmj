package cn.java.rlmj.sdkdo;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import cn.java.rlmj.hrsdk.AFD_FSDKLibrary;
import cn.java.rlmj.hrsdk.AFD_FSDK_FACERES;
import cn.java.rlmj.hrsdk.AFR_FSDKLibrary;
import cn.java.rlmj.hrsdk.AFR_FSDK_FACEINPUT;
import cn.java.rlmj.hrsdk.AFR_FSDK_FACEMODEL;
import cn.java.rlmj.hrsdk.ASVLOFFSCREEN;
import cn.java.rlmj.hrsdk.ASVL_COLOR_FORMAT;
import cn.java.rlmj.hrsdk.CLibrary;
import cn.java.rlmj.hrsdk.FaceInfo;
import cn.java.rlmj.hrsdk.MRECT;
import cn.java.rlmj.hrsdk._AFD_FSDK_OrientPriority;
import cn.java.rlmj.utils.BufferInfo;
import cn.java.rlmj.utils.ImageLoader;

import com.sun.jna.Memory;
import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.FloatByReference;
import com.sun.jna.ptr.PointerByReference;


/**
 * ����ʱ��Ҫ������init��Ȼ��free��
 * @version :v1.0.0
 * @description :
 * @author: zym
 * @date: 2018��2��7������11:38:19
 */
public class AFRmain{
	public static final String    APPID  = "FButrxH28fWTDJNwvE2bN5h1bDECmk8yTnFNjd2d32c4";
	public static final String FD_SDKKEY = "CPRdkcYKWNrDrWQ1rbPDb9DLTXzNyUGyCbC8HLCkn6YR";
	public static final String FR_SDKKEY = "CPRdkcYKWNrDrWQ1rbPDb9Dq7932p85fubdkxSDfyM4u";
	
    public static final int FD_WORKBUF_SIZE = 20 * 1024 * 1024;
    public static final int FR_WORKBUF_SIZE = 40 * 1024 * 1024;
    public static final int MAX_FACE_NUM = 50;//��������ܼ�����������

    public static final boolean bUseRAWFile = false;
    public static final boolean bUseBGRToEngine = true;
    public static byte[] model;
    public static Pointer hFDEngine;
    public static Pointer hFREngine;
    public static Pointer pFDWorkMem;
    public static Pointer pFRWorkMem;
    //��ʼ������
    public static boolean init(){
    	// init Engine
    	System.out.println("加载人脸引擎");
        pFDWorkMem = CLibrary.INSTANCE.malloc(FD_WORKBUF_SIZE);//��������ָ��ռ�
        pFRWorkMem = CLibrary.INSTANCE.malloc(FR_WORKBUF_SIZE);

        PointerByReference phFDEngine = new PointerByReference();
        NativeLong ret = AFD_FSDKLibrary.INSTANCE.AFD_FSDK_InitialFaceEngine(APPID, FD_SDKKEY, pFDWorkMem, FD_WORKBUF_SIZE, phFDEngine, _AFD_FSDK_OrientPriority.AFD_FSDK_OPF_0_HIGHER_EXT, 32, MAX_FACE_NUM);//��ʼ������
        if (ret.longValue() != 0) {
            CLibrary.INSTANCE.free(pFDWorkMem);
            CLibrary.INSTANCE.free(pFRWorkMem);
            System.out.println(String.format("AFD_FSDK_InitialFaceEngine ret 0x%x",ret.longValue()));
            return false;
           
        }

        // print FDEngine version
        hFDEngine = phFDEngine.getValue();
        //AFD_FSDK_Version versionFD = AFD_FSDKLibrary.INSTANCE.AFD_FSDK_GetVersion(hFDEngine);
        PointerByReference phFREngine = new PointerByReference();
        ret = AFR_FSDKLibrary.INSTANCE.AFR_FSDK_InitialEngine(APPID, FR_SDKKEY, pFRWorkMem, FR_WORKBUF_SIZE, phFREngine);
        if (ret.longValue() != 0) {
            AFD_FSDKLibrary.INSTANCE.AFD_FSDK_UninitialFaceEngine(hFDEngine);
            CLibrary.INSTANCE.free(pFDWorkMem);
            CLibrary.INSTANCE.free(pFRWorkMem);
            System.out.println(String.format("AFR_FSDK_InitialEngine ret 0x%x" ,ret.longValue()));
           return false;
        }

        // print FREngine version
        hFREngine = phFREngine.getValue();
        //AFR_FSDK_Version versionFR = AFR_FSDKLibrary.INSTANCE.AFR_FSDK_GetVersion(hFREngine);
        return true;
    }
    //�ͷ�ϵͳ��Դ
    public static void free(){
    	System.out.println("引擎关闭");
    	if(hFDEngine!=null)
        AFD_FSDKLibrary.INSTANCE.AFD_FSDK_UninitialFaceEngine(hFDEngine);
    	if(hFREngine!=null)
        AFR_FSDKLibrary.INSTANCE.AFR_FSDK_UninitialEngine(hFREngine);
        if(pFDWorkMem!=null)
        CLibrary.INSTANCE.free(pFDWorkMem);
        if(pFRWorkMem!=null)
        CLibrary.INSTANCE.free(pFRWorkMem);
    }
   //һ������ASVLOFFSCREEN�ĺ���
    public static ASVLOFFSCREEN changePic(String path){
    	System.out.println("图片地址"+path);
    	String[] msg=path.split("\\.");//ת��ָ�
    	//System.out.println(msg[1]);
    	if(msg[1].equals("YUV")||msg[1].equals("yuv")){
    		int yuv_widthA = 640;
            int yuv_heightA = 480;
            int yuv_formatA = ASVL_COLOR_FORMAT.ASVL_PAF_I420;
            return loadRAWImage(path, yuv_widthA, yuv_heightA, yuv_formatA);
    	}else{
    		return loadImage(path);
    	}
    }
    public static ASVLOFFSCREEN changePic(BufferedImage img){
    	
    		return loadImage(img);
    }
    //������ͼƬ���бȽ�
    public static float toCompare(String path1,String path2){
    	System.out.println("人脸比对");
        float similarity=0.0f;
        // load Image Data
        ASVLOFFSCREEN inputImgA=changePic(path1);
        ASVLOFFSCREEN inputImgB=changePic(path2);
        similarity=compareFaceSimilarity(inputImgA, inputImgB);
        return similarity;
    }
    //�����������󷵻����ƶ�
    public static float toCompare(AFR_FSDK_FACEMODEL face1,AFR_FSDK_FACEMODEL face2){
    	System.out.println("人脸比对");
    	 FloatByReference fSimilScore = new FloatByReference(0.0f);
         NativeLong ret = AFR_FSDKLibrary.INSTANCE.AFR_FSDK_FacePairMatching(hFREngine, face1, face2, fSimilScore);
         //face1.freeUnmanaged();
         //face2.freeUnmanaged();
         if (ret.longValue() != 0) {
             System.out.println(String.format("AFR_FSDK_FacePairMatching failed:ret 0x%x" ,ret.longValue()));
             return 0.0f;
         }
         return fSimilScore.getValue();
    }
    //ʶ�������
    public static FaceInfo[] doFaceDetection( ASVLOFFSCREEN inputImg) {
    	System.out.println("获取人脸");
        FaceInfo[] faceInfo = new FaceInfo[0];
       
        PointerByReference ppFaceRes = new PointerByReference();
        NativeLong ret = AFD_FSDKLibrary.INSTANCE.AFD_FSDK_StillImageFaceDetection(hFDEngine, inputImg, ppFaceRes);
        if (ret.longValue() != 0) {
            System.out.println(String.format("AFD_FSDK_StillImageFaceDetection ret 0x%x" , ret.longValue()));
            return faceInfo;
        }

        AFD_FSDK_FACERES faceRes = new AFD_FSDK_FACERES(ppFaceRes.getValue());
        System.out.println("一共有人脸数="+faceRes.nFace);
        if (faceRes.nFace > 0) {
        	//System.out.println("��������="+faceRes.nFace);
            faceInfo = new FaceInfo[faceRes.nFace];
            for (int i = 0; i < faceRes.nFace; i++) {
                MRECT rect = new MRECT(new Pointer(Pointer.nativeValue(faceRes.rcFace.getPointer()) + faceRes.rcFace.size() * i));
                int orient = faceRes.lfaceOrient.getPointer().getInt(i * 4);
                faceInfo[i] = new FaceInfo();

                faceInfo[i].left = rect.left;
                faceInfo[i].top = rect.top;
                faceInfo[i].right = rect.right;
                faceInfo[i].bottom = rect.bottom;
                faceInfo[i].orient = orient;

                //System.out.println(String.format("%d (%d %d %d %d) orient %d", i, rect.left, rect.top, rect.right, rect.bottom, orient));
            }
        }
     
        return faceInfo;
    }

    public static AFR_FSDK_FACEMODEL extractFRFeature(ASVLOFFSCREEN inputImg, FaceInfo faceInfo) {
    	System.out.println("获取特征人脸");
        AFR_FSDK_FACEINPUT faceinput = new AFR_FSDK_FACEINPUT();
        faceinput.lOrient = faceInfo.orient;
        faceinput.rcFace.left = faceInfo.left;
        faceinput.rcFace.top = faceInfo.top;
        faceinput.rcFace.right = faceInfo.right;
        faceinput.rcFace.bottom = faceInfo.bottom;

        AFR_FSDK_FACEMODEL faceFeature = new AFR_FSDK_FACEMODEL();
        NativeLong ret = AFR_FSDKLibrary.INSTANCE.AFR_FSDK_ExtractFRFeature(hFREngine, inputImg, faceinput, faceFeature);
        if (ret.longValue() != 0) {
            System.out.println(String.format("AFR_FSDK_ExtractFRFeature ret 0x%x" ,ret.longValue()));
            return null;
        }

        try {
            return faceFeature.deepCopy();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    //��ȡ��������
    public static AFR_FSDK_FACEMODEL getFaceModel(ASVLOFFSCREEN inputImg){
    	FaceInfo[] faceInfosA = doFaceDetection( inputImg);
        if (faceInfosA.length < 1) {
            System.out.println("no face in Image");
            return null;
        }
        AFR_FSDK_FACEMODEL faceFeature = extractFRFeature( inputImg, faceInfosA[0]);
        try {
			return faceFeature.deepCopy();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
    }
   /* public static void main(String args[]) throws Exception{
		AFRmain afr=new AFRmain();
		afr.init();
		ASVLOFFSCREEN inputImg=afr.changePic("hezhao.jpg");
    	FaceInfo[] faceInfosA = afr.doFaceDetection( inputImg);
        if (faceInfosA.length < 1) {
            System.out.println("no face in Image");
            return;
        }
           
       AFR_FSDK_FACEMODEL a= afr.extractFRFeature( inputImg, faceInfosA[0]);
       AFR_FSDK_FACEMODEL b= afr.extractFRFeature( inputImg, faceInfosA[1]);
       
        System.out.println(String.format("similarity between faceA and faceB is %f" , afr.toCompare(a, b)));
        	
        
        	
         a.freeUnmanaged();
          b.freeUnmanaged();
		afr.free();
	}*/
    public static AFR_FSDK_FACEMODEL getFaceModel(String path){
    	ASVLOFFSCREEN inputImg=changePic(path);
    	FaceInfo[] faceInfosA = doFaceDetection( inputImg);
        if (faceInfosA.length < 1) {
            System.out.println("no face in Image");
            return null;
        }
        AFR_FSDK_FACEMODEL faceFeature = extractFRFeature( inputImg, faceInfosA[0]);
		return faceFeature;
    }
    public static float compareFaceSimilarity( ASVLOFFSCREEN inputImgA, ASVLOFFSCREEN inputImgB) {
        // Do Face Detect
    	System.out.println("人脸比对");
        FaceInfo[] faceInfosA = doFaceDetection(inputImgA);
        if (faceInfosA.length < 1) {
            System.out.println("no face in Image A ");
            return 0.0f;
        }

        FaceInfo[] faceInfosB = doFaceDetection(inputImgB);
        if (faceInfosB.length < 1) {
            System.out.println("no face in Image B ");
            return 0.0f;
        }

        // Extract Face Feature
        AFR_FSDK_FACEMODEL faceFeatureA = extractFRFeature(inputImgA, faceInfosA[0]);
        if (faceFeatureA == null) {
            System.out.println("extract face feature in Image A failed");
            return 0.0f;
        }
        System.out.println(faceFeatureA.lFeatureSize);
        AFR_FSDK_FACEMODEL faceFeatureB = extractFRFeature(inputImgB, faceInfosB[0]);
        if (faceFeatureB == null) {
            System.out.println("extract face feature in Image B failed");
            faceFeatureA.freeUnmanaged();
            return 0.0f;
        }
        try {
			model=faceFeatureB.toByteArray();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        // calc similarity between faceA and faceB
        FloatByReference fSimilScore = new FloatByReference(0.0f);
        NativeLong ret = AFR_FSDKLibrary.INSTANCE.AFR_FSDK_FacePairMatching(hFREngine, faceFeatureA, faceFeatureB, fSimilScore);
        faceFeatureA.freeUnmanaged();
        faceFeatureB.freeUnmanaged();
        if (ret.longValue() != 0) {
            System.out.println(String.format("AFR_FSDK_FacePairMatching failed:ret 0x%x" ,ret.longValue()));
            return 0.0f;
        }
        return fSimilScore.getValue();
    }

    public static ASVLOFFSCREEN loadRAWImage(String yuv_filePath, int yuv_width, int yuv_height, int yuv_format) {
        int yuv_rawdata_size = 0;

        ASVLOFFSCREEN inputImg = new ASVLOFFSCREEN();
        inputImg.u32PixelArrayFormat = yuv_format;
        inputImg.i32Width = yuv_width;
        inputImg.i32Height = yuv_height;
        if (ASVL_COLOR_FORMAT.ASVL_PAF_I420 == inputImg.u32PixelArrayFormat) {
            inputImg.pi32Pitch[0] = inputImg.i32Width;
            inputImg.pi32Pitch[1] = inputImg.i32Width / 2;
            inputImg.pi32Pitch[2] = inputImg.i32Width / 2;
            yuv_rawdata_size = inputImg.i32Width * inputImg.i32Height * 3 / 2;
        } else if (ASVL_COLOR_FORMAT.ASVL_PAF_NV12 == inputImg.u32PixelArrayFormat) {
            inputImg.pi32Pitch[0] = inputImg.i32Width;
            inputImg.pi32Pitch[1] = inputImg.i32Width;
            yuv_rawdata_size = inputImg.i32Width * inputImg.i32Height * 3 / 2;
        } else if (ASVL_COLOR_FORMAT.ASVL_PAF_NV21 == inputImg.u32PixelArrayFormat) {
            inputImg.pi32Pitch[0] = inputImg.i32Width;
            inputImg.pi32Pitch[1] = inputImg.i32Width;
            yuv_rawdata_size = inputImg.i32Width * inputImg.i32Height * 3 / 2;
        } else if (ASVL_COLOR_FORMAT.ASVL_PAF_YUYV == inputImg.u32PixelArrayFormat) {
            inputImg.pi32Pitch[0] = inputImg.i32Width * 2;
            yuv_rawdata_size = inputImg.i32Width * inputImg.i32Height * 2;
        } else if (ASVL_COLOR_FORMAT.ASVL_PAF_RGB24_B8G8R8 == inputImg.u32PixelArrayFormat) {
            inputImg.pi32Pitch[0] = inputImg.i32Width * 3;
            yuv_rawdata_size = inputImg.i32Width * inputImg.i32Height * 3;
        } else {
            System.out.println("unsupported  yuv format");
            System.exit(0);
        }

        // load YUV Image Data from File
        byte[] imagedata = new byte[yuv_rawdata_size];
        File f = new File(yuv_filePath);
        InputStream ios = null;
        try {
            ios = new FileInputStream(f);
            ios.read(imagedata,0,yuv_rawdata_size);

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("error in loading yuv file");
            System.exit(0);
        } finally {
            try {
                if (ios != null) {
                    ios.close();
                }
            } catch (IOException e) {
            }
        }

        if (ASVL_COLOR_FORMAT.ASVL_PAF_I420 == inputImg.u32PixelArrayFormat) {
            inputImg.ppu8Plane[0] = new Memory(inputImg.pi32Pitch[0] * inputImg.i32Height);
            inputImg.ppu8Plane[0].write(0, imagedata, 0, inputImg.pi32Pitch[0] * inputImg.i32Height);
            inputImg.ppu8Plane[1] = new Memory(inputImg.pi32Pitch[1] * inputImg.i32Height / 2);
            inputImg.ppu8Plane[1].write(0, imagedata, inputImg.pi32Pitch[0] * inputImg.i32Height, inputImg.pi32Pitch[1] * inputImg.i32Height / 2);
            inputImg.ppu8Plane[2] = new Memory(inputImg.pi32Pitch[2] * inputImg.i32Height / 2);
            inputImg.ppu8Plane[2].write(0, imagedata, inputImg.pi32Pitch[0] * inputImg.i32Height + inputImg.pi32Pitch[1] * inputImg.i32Height / 2, inputImg.pi32Pitch[2] * inputImg.i32Height / 2);
            inputImg.ppu8Plane[3] = Pointer.NULL;
        } else if (ASVL_COLOR_FORMAT.ASVL_PAF_NV12 == inputImg.u32PixelArrayFormat) {
            inputImg.ppu8Plane[0] = new Memory(inputImg.pi32Pitch[0] * inputImg.i32Height);
            inputImg.ppu8Plane[0].write(0, imagedata, 0, inputImg.pi32Pitch[0] * inputImg.i32Height);
            inputImg.ppu8Plane[1] = new Memory(inputImg.pi32Pitch[1] * inputImg.i32Height / 2);
            inputImg.ppu8Plane[1].write(0, imagedata, inputImg.pi32Pitch[0] * inputImg.i32Height, inputImg.pi32Pitch[1] * inputImg.i32Height / 2);
            inputImg.ppu8Plane[2] = Pointer.NULL;
            inputImg.ppu8Plane[3] = Pointer.NULL;
        } else if (ASVL_COLOR_FORMAT.ASVL_PAF_NV21 == inputImg.u32PixelArrayFormat) {
            inputImg.ppu8Plane[0] = new Memory(inputImg.pi32Pitch[0] * inputImg.i32Height);
            inputImg.ppu8Plane[0].write(0, imagedata, 0, inputImg.pi32Pitch[0] * inputImg.i32Height);
            inputImg.ppu8Plane[1] = new Memory(inputImg.pi32Pitch[1] * inputImg.i32Height / 2);
            inputImg.ppu8Plane[1].write(0, imagedata, inputImg.pi32Pitch[0] * inputImg.i32Height, inputImg.pi32Pitch[1] * inputImg.i32Height / 2);
            inputImg.ppu8Plane[2] = Pointer.NULL;
            inputImg.ppu8Plane[3] = Pointer.NULL;
        } else if (ASVL_COLOR_FORMAT.ASVL_PAF_YUYV == inputImg.u32PixelArrayFormat) {
            inputImg.ppu8Plane[0] = new Memory(inputImg.pi32Pitch[0] * inputImg.i32Height);
            inputImg.ppu8Plane[0].write(0, imagedata, 0, inputImg.pi32Pitch[0] * inputImg.i32Height);
            inputImg.ppu8Plane[1] = Pointer.NULL;
            inputImg.ppu8Plane[2] = Pointer.NULL;
            inputImg.ppu8Plane[3] = Pointer.NULL;
        } else if (ASVL_COLOR_FORMAT.ASVL_PAF_RGB24_B8G8R8 == inputImg.u32PixelArrayFormat) {
            inputImg.ppu8Plane[0] = new Memory(imagedata.length);
            inputImg.ppu8Plane[0].write(0, imagedata, 0, imagedata.length);
            inputImg.ppu8Plane[1] = Pointer.NULL;
            inputImg.ppu8Plane[2] = Pointer.NULL;
            inputImg.ppu8Plane[3] = Pointer.NULL;
        } else {
            System.out.println("unsupported yuv format");
            System.exit(0);
        }

        inputImg.setAutoRead(false);
        return inputImg;
    }

    public static ASVLOFFSCREEN loadImage(String filePath) {
        ASVLOFFSCREEN inputImg = new ASVLOFFSCREEN();

        if (bUseBGRToEngine) {
            BufferInfo bufferInfo = ImageLoader.getBGRFromFile(filePath);
            inputImg.u32PixelArrayFormat = ASVL_COLOR_FORMAT.ASVL_PAF_RGB24_B8G8R8;
            inputImg.i32Width = bufferInfo.width;
            inputImg.i32Height = bufferInfo.height;
            inputImg.pi32Pitch[0] = inputImg.i32Width * 3;
            inputImg.ppu8Plane[0] = new Memory(inputImg.pi32Pitch[0] * inputImg.i32Height);
            inputImg.ppu8Plane[0].write(0, bufferInfo.buffer, 0, inputImg.pi32Pitch[0] * inputImg.i32Height);
            inputImg.ppu8Plane[1] = Pointer.NULL;
            inputImg.ppu8Plane[2] = Pointer.NULL;
            inputImg.ppu8Plane[3] = Pointer.NULL;
        } else {
            BufferInfo bufferInfo = ImageLoader.getI420FromFile(filePath);
            inputImg.u32PixelArrayFormat = ASVL_COLOR_FORMAT.ASVL_PAF_I420;
            inputImg.i32Width = bufferInfo.width;
            inputImg.i32Height = bufferInfo.height;
            inputImg.pi32Pitch[0] = inputImg.i32Width;
            inputImg.pi32Pitch[1] = inputImg.i32Width / 2;
            inputImg.pi32Pitch[2] = inputImg.i32Width / 2;
            inputImg.ppu8Plane[0] = new Memory(inputImg.pi32Pitch[0] * inputImg.i32Height);
            inputImg.ppu8Plane[0].write(0, bufferInfo.buffer, 0, inputImg.pi32Pitch[0] * inputImg.i32Height);
            inputImg.ppu8Plane[1] = new Memory(inputImg.pi32Pitch[1] * inputImg.i32Height / 2);
            inputImg.ppu8Plane[1].write(0, bufferInfo.buffer, inputImg.pi32Pitch[0] * inputImg.i32Height, inputImg.pi32Pitch[1] * inputImg.i32Height / 2);
            inputImg.ppu8Plane[2] = new Memory(inputImg.pi32Pitch[2] * inputImg.i32Height / 2);
            inputImg.ppu8Plane[2].write(0, bufferInfo.buffer, inputImg.pi32Pitch[0] * inputImg.i32Height + inputImg.pi32Pitch[1] * inputImg.i32Height / 2, inputImg.pi32Pitch[2] * inputImg.i32Height / 2);
            inputImg.ppu8Plane[3] = Pointer.NULL;
        }

        inputImg.setAutoRead(false);
        return inputImg;
    }
    public static ASVLOFFSCREEN loadImage(BufferedImage img) {
        ASVLOFFSCREEN inputImg = new ASVLOFFSCREEN();

        if (bUseBGRToEngine) {
            BufferInfo bufferInfo = ImageLoader.getBGRFromFile(img);
            inputImg.u32PixelArrayFormat = ASVL_COLOR_FORMAT.ASVL_PAF_RGB24_B8G8R8;
            inputImg.i32Width = bufferInfo.width;
            inputImg.i32Height = bufferInfo.height;
            inputImg.pi32Pitch[0] = inputImg.i32Width * 3;
            inputImg.ppu8Plane[0] = new Memory(inputImg.pi32Pitch[0] * inputImg.i32Height);
            inputImg.ppu8Plane[0].write(0, bufferInfo.buffer, 0, inputImg.pi32Pitch[0] * inputImg.i32Height);
            inputImg.ppu8Plane[1] = Pointer.NULL;
            inputImg.ppu8Plane[2] = Pointer.NULL;
            inputImg.ppu8Plane[3] = Pointer.NULL;
        } else {
            BufferInfo bufferInfo = ImageLoader.getI420FromFile(img);
            inputImg.u32PixelArrayFormat = ASVL_COLOR_FORMAT.ASVL_PAF_I420;
            inputImg.i32Width = bufferInfo.width;
            inputImg.i32Height = bufferInfo.height;
            inputImg.pi32Pitch[0] = inputImg.i32Width;
            inputImg.pi32Pitch[1] = inputImg.i32Width / 2;
            inputImg.pi32Pitch[2] = inputImg.i32Width / 2;
            inputImg.ppu8Plane[0] = new Memory(inputImg.pi32Pitch[0] * inputImg.i32Height);
            inputImg.ppu8Plane[0].write(0, bufferInfo.buffer, 0, inputImg.pi32Pitch[0] * inputImg.i32Height);
            inputImg.ppu8Plane[1] = new Memory(inputImg.pi32Pitch[1] * inputImg.i32Height / 2);
            inputImg.ppu8Plane[1].write(0, bufferInfo.buffer, inputImg.pi32Pitch[0] * inputImg.i32Height, inputImg.pi32Pitch[1] * inputImg.i32Height / 2);
            inputImg.ppu8Plane[2] = new Memory(inputImg.pi32Pitch[2] * inputImg.i32Height / 2);
            inputImg.ppu8Plane[2].write(0, bufferInfo.buffer, inputImg.pi32Pitch[0] * inputImg.i32Height + inputImg.pi32Pitch[1] * inputImg.i32Height / 2, inputImg.pi32Pitch[2] * inputImg.i32Height / 2);
            inputImg.ppu8Plane[3] = Pointer.NULL;
        }

        inputImg.setAutoRead(false);
        return inputImg;
    }

}
