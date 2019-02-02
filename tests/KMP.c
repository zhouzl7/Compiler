//#include <stdio.h>
//#include <stdlib.h>
//#include <string.h>
//#define MAXSIZE 1024

void get_next(char T[], int next[]) //修正前的next数组
{
    int i = 1, j = 0;
    next[0] = -1;
    next[1] = 0;
    int m = strlen(T);
    while (i < strlen(T) - 1)
    {
        if (j == -1 || T[j] == T[i])
        {
            i++;
            j++;
            next[i] = j;
        }
        else
            j = next[j];
    }
}

void get_nextval(char T[], int nextval[]) //修正后的nextval数组
{
    int i = 1, j = 0;
    nextval[0] = -1;
    nextval[1] = 0;
    int m = strlen(T);
    while (i < strlen(T) - 1)
    {
        if (j == -1 || T[j] == T[i])
        {
            i++;
            j++;
            if (T[i] != T[j])
                nextval[i] = j;
            else
                nextval[i] = nextval[j];
        }
        else
            j = nextval[j];
    }
}

int Index_kmp(char S[], char T[], int pos, int next[]) //逐项比较
{
    int j = 0, i = pos, lens = strlen(S), lent = strlen(T);
    get_nextval(T, next);
    while (i < lens && j < lent)
    {
        if (S[i] == T[j] || j == -1)
        {
            i++;
            j++;
        }
        else
            j = next[j];
    }
    if (j >= lent)
        return i - lent;
    else
        return -1;
}

int main(int argc, char *argv[])
{
    char S[1024], T[1024];
    if (argc < 3)
    {
        printf("Usage: kmp <string> <pattern>\n");
        return 0;
    }
    strcpy(S, argv[1]);
    strcpy(T, argv[2]);
    int m;
    int nextval[1024]; //修正后的nextval数组

    int t = Index_kmp(S, T, 0, nextval);
    if (t == -1)
        printf("False\n");
    else
    {
        printf("%d", t);
        t = Index_kmp(S, T, t + 1, nextval);
        while (t != -1)
        {
            printf(", %d", t);
            t = Index_kmp(S, T, t + 1, nextval);
        }
    }
    printf("\n");
    return 0;
}