/* Start Main function and pass arguments from here */
main(process.argv.length - 1, process.argv.splice(1));


/* Some System functions */
function exit(code = 0) {
  process.exit(code);
}
