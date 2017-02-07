/*************************************************************************
     File Name: AandBToC.c
     Author: fht
     Mail: fht@microarray.com.cn
     Created Time: 2017年02月07日 星期二 16时06分18秒
     Funch: 从文件A 和B 各存放一行字母，要求把两个文件中的信息合并，输出到一个新文件C中
 ************************************************************************/

#include<stdio.h>

int main(){
    FILE *fp;
    int i,j,n,ni;
    char c[160], t, ch;
    if((fp = fopen("A","r")) == NULL){
        printf("file A cannot be opened\n");
        exit(0);
    }
    printf("\n A contents are: \n");
    for(i=0;(ch=fgetc(fp))!=EOF;i++){
        c[i] = ch;
        putchar(c[i]);
    }
    fclose(fp);
    ni = i;

    if((fp=fopen("B","r"))==NULL){
        printf("file B cannot be opened\n");
        exit(0);
    }
    printf("\n B contents are : \n");
    for(i=ni;(ch=fgetc(fp))!=EOF;i++){
        c[i] = ch;
        putchar(c[i]);
    }
    fclose(fp);
    n = i;

    for(i=0;i<n;i++){
        for(j=i+1;j<n;j++){
            if(c[i] <  c[j]){
                t = c[i];
                c[i] = c[j];
                c[j] = t;
            }
        }
    }
    printf("\n C file is :\n");
    fp = fopen("C", "w");
    for(i=0;i<n;i++){
        putc(c[i],fp);
        putchar(c[i]);
    }
    fclose(fp);

    printf("\n");
    return 0;
}
