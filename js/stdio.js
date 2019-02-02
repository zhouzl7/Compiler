/* stdio.js
 * Support some IO function in C
 */

function printf(fmt, ...args) {
  let util = require("util");
  let str = util.format(fmt, ...args);
  process.stdout.write(Buffer(str));
  return str.length;
}
