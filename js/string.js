/* string.js
 * Support some string function in C
 *
 */

/* strlen
 * @param s: String or Array
 *
 */
function strlen(s) {
  if (typeof s === "string") return s.length;
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
