//#define _CRT_SECURE_NO_WARNINGS

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
int OK = 100001;
int ERROR = 100000;
int num[1024], opt[1024];

int InitStack(int l[])
{
    l[0] = 0;

    return OK;
}

int EmptyStack(int s[])
{
    return (s[0] == 0) ? OK : ERROR;
}

int Push(int s[], int e)
{
    s[0]++;
    s[s[0]] = e;
    return OK;
}

int GetTop(int s[])
{
    if (s[0] == 0)
    {
        printf("Error in GetTop :Stack is empty!\n");
        exit(1);
        return ERROR;
    }

    return s[s[0]];
}

int Priority(char s)
{
    switch (s)
    {
    case '(':
        return 3;
    case '*':
    case '/':
        return 2;
    case '+':
    case '-':
        return 1;
    default:
        return 0;
    }
}

int char2int(char c) 
{
	return (c - '0');
}

int Pop(int s[])
{
    int e;

    if (s[0] == 0)
    {
        printf("Error in Pop :Stack is empty!\n");
        exit(1);
        return ERROR;
    }

    e = s[s[0]];
    s[0]--;
    return e;
}

int main(int argc, char *argv[])
{
    //int num[1024], opt[1024];

    char str[1024];
    int i = 0, tmp = 0, j;
    
    if (InitStack(num) != OK || InitStack(opt) != OK)
    {
        printf("Init Failure!\n");
    }

    if (argc < 2)
    {
        printf("Usage: Calculator <string>\n");
        return 0;
    }
    strcpy(str, argv[1]);
    while (i < strlen(str) || EmptyStack(opt) != OK)
    {
        if (str[i] >= '0' && str[i] <= '9')
        {
<<<<<<< HEAD
            tmp = (tmp * 10) + (str[i] - '0');
=======
            tmp = tmp * 10 + char2int(str[i]);
>>>>>>> 9e5312b... Update Calculator.c
            i++;
            if (str[i] < '0' || str[i] > '9' || i == strlen(str))
            {
                Push(num, tmp);
                tmp = 0;
            }
        }
        else
        {
          
            if ((EmptyStack(opt) == OK) || (GetTop(opt) == '(' && str[i] != ')') || Priority(str[i]) > Priority(GetTop(opt))) //进栈不参与运算
            {
                Push(opt, str[i]);
                i++;
                continue;
            }
            if (GetTop(opt) == '(' && str[i] == ')') //出栈不参与运算
            {
                Pop(opt);
                i++;
                continue;
            }
            if ((i == strlen(str) && EmptyStack(opt) != OK) || (str[i] == ')' && GetTop(opt) != '(') || Priority(str[i]) <= Priority(GetTop(opt))) //出栈并参与运算
            {
                switch (Pop(opt))
                {
                case '+':
                    Push(num, Pop(num) + Pop(num));
                    break;
                case '-':
                    j = Pop(num);
                    Push(num, Pop(num) - j);
                    break;
                case '*':
                    Push(num, Pop(num) * Pop(num));
                    break;
                case '/':
                    j = Pop(num);
                    Push(num, Pop(num) / j);
					break;
                }
                continue;
            }
        }
    }
    printf("%d\n", Pop(num));
    return 0;
}