let bilangan1 = parseInt(prompt("Masukkan bilangan ke-1:"));
let bilangan2 = parseInt(prompt("Masukkan bilangan ke-2:"));

if (bilangan1 > bilangan2) {
    document.write("Bilangan ke-1 lebih besar dari bilangan ke-2.");
} 
else if (bilangan1 < bilangan2) {
    document.write("Bilangan ke-2 lebih besar dari bilangan ke-1.");
} 
else {
    document.write("Kedua bilangan sama.");
}