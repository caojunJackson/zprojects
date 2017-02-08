/*************************************************************************
     File Name: quick_sort.c
     Author: fht
     Mail: fht@microarray.com.cn
     Created Time: 2017年02月08日 星期三 17时04分08秒

     通过一躺排序将要排序的数据分割成独立的两部分，其中一部分的所有数据都比另外一部分的所有数据都要小，然后再按次方法对这两部分数据分别进行快速排序，整个排序过程可以递归进行，以此达到整个数据变成有序序列。
     假设要排序的数组是A[0]……A[N-1]，首先任意选取一个数据（通常选用第一个数据）作为关键数据(key)，然后将所有比它的数都放到它前面，所有比它大的数都放到它后面，这个过程称为一躺快速排序。一躺快速排序的算法是：
     1）、设置两个变量i、j，排序开始的时候i：=1，j：= N；
     2）以第一个数组元素作为关键数据，赋值给X，即X：=A[0]；
     3）、从J开始向前搜索，即由后开始向前搜索（J：=J-1），找到第一个小于X的值，两者交换；
     4）、从I开始向后搜索，即由前开始向后搜索（I：=I+1），找到第一个大于X的值，两者交换；
     5）、重复第3、4步，直到i=j (while i == j)；
 ************************************************************************/

#include<stdio.h>

void quick_sort(int s[],int left, int right){
    int i = 0, j, x,k;
    printf("\nquick sort left = %d, right = %d \n",left,right);
    if(left <  right){
        i = left;
        j = right;
        x = s[i];
        while (i<j){
            while (i<j && s[j] > x)
                j--; /* 从右向左找第一个小于x的数 */
            if(i<j)
                s[i++] = s[j];

            while (i<j && s[i] < x)
                i++; /* 从左向右找第一个大于x的数 */
            if(i<j)
                s[j--] = s[i];
        }
        s[i] = x;
        for(k=0;k<7;k++){
            printf("%d ", s[k]);
        }
        printf("\n");
        quick_sort(s,left, i-1); /*递归调用*/
        quick_sort(s,i+1, right);
    }
}

int main(){
    int a[] = {49,38,65,97,76,13,27};
    int left = 0;
    int right = 6;
    int i = 0;
    printf("start ");
    for(i = 0; i<7;i++){
        printf("%d ",a[i]);
    }
    printf("\n");
    quick_sort(a, left, right);

    printf("sort ");
    for(i = 0; i<7;i++){
        printf("%d ",a[i]);
    }
    printf("\n");
    return 0;
}
