#!/usr/bin/node

/* stdio.js 
* Support some IO function in C
*
*/

function printf(fmt, ...args) {
    let util = require('util');
    let str = util.format(fmt, ...args);
    process.stdout.write(Buffer(str));
    return str.length;
}


/* string.js
* Support some string function in C
*
*/

/* strlen
* @param s: String or Array
* 
*/
function strlen(s) {
    if (typeof (s) === 'string')
        return s.length;
    else {
        let i = 0;
        while (s[i] !== undefined) {
            i++;
        }
        return i;
    }
}

function strcpy(dst, src) {
    for (let i = 0; i < src.length; i++) {
        dst[i] = src[i];
    }
    return src.length;
}

var OK=100001;var ERROR=100000;var num = new Array(1024), opt = new Array(1024);function InitStack(l){l[0]=0;return OK;}function EmptyStack(s){return (s[0]==0)?OK:ERROR;}function Push(s, e){s[0]++;s[s[0]]=e;return OK;}function GetTop(s){if  (s[0]==0){return ERROR;}return s[s[0]];}function Priority(s){switch (s){case '(': return 3;case '*': case '/': return 2;case '+': case '-': return 1;default: return 0;}}function Pop(s){var e;if  (s[0]==0){return ERROR;}e=s[s[0]];s[0]--;return e;}function main(argc, argv){var str = new Array(1024);var i=0,tmp=0,j;if  (InitStack(num)!=OK||InitStack(opt)!=OK){printf("Init Failure!\n");}if  (argc<2){printf("Usage: Calculator <string>");return 0;}strcpy(str, argv[1]);while (i<strlen(str)||EmptyStack(opt)!=OK){if  (str[i]>='0'&&str[i]<='9'){tmp=tmp*10+str[i]-'0';i++;if  (str[i]<'0'||str[i]>'9'){Push(num, tmp);tmp=0;}}else {if  ((EmptyStack(opt)==OK)||(GetTop(opt)=='('&&str[i]!=')')||Priority(str[i])>Priority(GetTop(opt))){Push(opt, str[i]);i++;continue;}if  (GetTop(opt)=='('&&str[i]==')'){Pop(opt);i++;continue;}if  ((i==strlen(i)&&EmptyStack(opt)!=OK)||(str[i]==')'&&GetTop(opt)!='(')||Priority(str[i])<=Priority(GetTop(opt))){switch (Pop(opt)){case '+': Push(num, Pop(num)+Pop(num));break;case '-': j=Pop(num);Push(num, Pop(num)-j);break;case '*': Push(num, Pop(num)*Pop(num));break;case '/': j=Pop(num);Push(num, Pop(num)/j);}continue;}}}printf("%d", Pop(num));printf("\n");return 0;}

/* Start Main function and pass arguments from here */
main(process.argv.length-1, process.argv.splice(1));