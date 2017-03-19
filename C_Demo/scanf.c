/*************************************************************************
     File Name: scanf.c
     Author: fht
     Mail: fht@microarray.com.cn
     Created Time: 2017年03月19日 星期日 19时57分59秒
     scanf 多参数输入格式符是用"," 或" ", 在输入时即使用对应的格式间隔符
 ************************************************************************/

#include<stdio.h>
int main(int argc, char *argv[]){
    int result;
    char str[128];
    char c;
    int input;

    printf("请按空格键间隔输入:\n");
    result = scanf("%c %d %s",&c, &input, str);
    printf("c=%c, input=%d, str=%s , result=%d\n", c, input, str, result);

    return 0;
}
