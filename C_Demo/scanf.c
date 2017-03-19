/*************************************************************************
     File Name: scanf.c
     Author: fht
     Mail: fht@microarray.com.cn
     Created Time: 2017年03月19日 星期日 19时57分59秒
     scanf 多参数输入格式符是用"," 或" ", 在输入时即使用对应的格式间隔符
 ************************************************************************/

#include<stdio.h>

void input1(){
    char str[128];
    char c;
    while(1){
        printf("请选择你的操作:w 输入 q 退出 其他退出:\n");
        c=getchar();
        if(c=='q'){
            break;
        }else if(c=='w'){
            scanf("%s",str);
            getchar();//读入并忽略掉缓冲区内的空白字符
            printf("scanf = %s\n",str);
        }else{
            printf("你选择错误,退出!\n");
            break;
        }
    }
}

int main(int argc, char *argv[]){
    int result;
    char str[128];
    char c;
    int input;
/*
    printf("请按空格键间隔输入:\n");
    result = scanf("%c %d %s",&c, &input, str);
    printf("c=%c, input=%d, str=%s , result=%d\n", c, input, str, result);
*/
    input1();
    return 0;
}
