
int main(int argc, char* argv[])
{
    char string[200];
    if(argc<2){
        printf("Usage: Pamlindrome <string>\n");
        return 0;
    }
    strcpy(string,argv[1]);
    int lenght = strlen(string);
    for (int i = 0; i < lenght / 2; i++)
    {
        if (string[i] != string[lenght - i -1])
        {
            printf("False\n");
            return 0;
        }
    }
    printf("True\n");
    return 0;
}