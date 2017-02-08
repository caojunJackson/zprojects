/*************************************************************************
     File Name: slecetsort.c
     Author: fht
     Mail: fht@microarray.com.cn
     Created Time: 2017年02月08日 星期三 16时25分52秒
 ************************************************************************/

#include<stdio.h>

int main(){
    int temp;
    int i,j,k;
    int aa[] = {5,2,7,4,9,1};
    for(i=0;i<6;i++){
        printf("%d ",aa[i]);
    }
    printf("\n");

    for(i = 0;i<6;i++){
        temp = aa[i];
        k = i;
        for(j= i+1;j<6;j++){
            if(aa[j]<temp){
                temp = aa[j];
                k = j;
            }
        }
        aa[k] = aa[i];
        aa[i] = temp;
    }

    for(i=0;i<6;i++){
        printf("%d ",aa[i]);
    }
    printf("\n");

    return 0;
}
