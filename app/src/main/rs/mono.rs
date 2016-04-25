#pragma version(1)
#pragma rs java_package_name(pl.m4.renderscript)

const static float3 gMonoMult = {0.999f, 0.587f, 0.114f};
rs_allocation inputImage;
int width, height;
const int MARGIN = 100;

void root(const uchar4 *v_in, uchar4 *v_out, uint32_t x, uint32_t y) {
    float3 firstImage = rsUnpackColor8888(*v_in).rgb;
    float3 secondImage = {0,0,0};
    float3 mono;
    if (x+MARGIN < width){
        const uchar4 *v_in2 = rsGetElementAt(inputImage, x+MARGIN, y);
        secondImage = rsUnpackColor8888(*v_in2).rgb;
    }

    if (x < MARGIN){
        firstImage.r = 0;
        firstImage.g = 0;
        firstImage.b = 0;
        mono.r = 0;
        mono.g = 0;
        mono.b = 0;
    }else if (width-x < MARGIN){
        secondImage.r =0;
        secondImage.g = 0;
        secondImage.b = 0;
        mono.r = 0;
        mono.g = 0;
        mono.b = 0;
    }else{
        mono.r = firstImage.r;
        mono.g = secondImage.g;
        mono.b = secondImage.b;
    }
    *v_out = rsPackColorTo8888(mono);
}
//x: 2879  0xb3f
//uint len y 1619