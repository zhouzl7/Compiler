struct Point
{
    int x;
    int y;
};
int cc[10];
//int printInt(const char[], int); //not supported?


int main()
{
    int x[100];
    x[10] = 0;
    struct Point p, q;
    int a, b, c;
    a = (b + c) * (b - c);
    int i;
    p.x = a == b ? 1 : -1;
    p.y = 233;
    for (i = 0; i < 100; i++)
    {
        if (i % 2 == 0)
            printf("%d\n", p.x);
        else
            printf("%d\n", p.y);
    }
    switch (a)
    {
    case 0x1234:
        break;
    case 123:
        break;
    default:
        break;
    }
    return a;
}