#pragma version(1)
#pragma rs java_package_name(pl.m4.renderscript)

const static float3 gMonoMult = {0.999f, 0.587f, 0.114f};
rs_allocation inputImage;

void root(const uchar4 *v_in, uchar4 *v_out, uint32_t x, uint32_t y) {
    float3 firstImage = rsUnpackColor8888(*v_in).rgb;
    float3 secondImage = {0,0,0};
    if (x+100 < 2879 && y +100 < 1619){
        const uchar4 *v_in2 = rsGetElementAt(inputImage, x+100, y+100);
        secondImage = rsUnpackColor8888(*v_in2).rgb;
    }
    float3 mono;
    if (x < 100){
        firstImage.r = 0;
        firstImage.g = 0;
        firstImage.b = 0;
        mono.r = 0;
        mono.g = 0;
        mono.b = 0;
    }else if (2879-x < 100){
        secondImage.r =0;
        secondImage.g = 0;
        secondImage.b = 0;
        mono.r = 0;
        mono.g = 0;
        mono.b = 0;
    }else{
    //float3 mono = firstImage + secondImage;
        mono.r = firstImage.r;
        mono.g = secondImage.g;
        mono.b = secondImage.b;
    }
    *v_out = rsPackColorTo8888(mono);
}
//int redRes = (int)(redL);
 // 	    	int greenRes = (int)(greenR);
  //	    	int blueRes = (int)(blueR);
//x: 2879  0xb3f
// uint len y 1619