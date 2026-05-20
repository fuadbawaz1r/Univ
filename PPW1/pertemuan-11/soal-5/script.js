function hitung(operator) {
    let angka1 = parseFloat(document.getElementById("angka1").value);
    let angka2 = parseFloat(document.getElementById("angka2").value);
    let hasil;

    if (isNaN(angka1) || isNaN(angka2)) {
        hasil = "Masukkan kedua angka terlebih dahulu!";
    } else {
        if (operator === "+") {
            hasil = angka1 + angka2;
        } else if (operator === "-") {
            hasil = angka1 - angka2;
        } else if (operator === "*") {
            hasil = angka1 * angka2;
        } else if (operator === "/") {
            if (angka2 === 0) {
                hasil = "Tidak bisa dibagi 0!";
            } else {
                hasil = angka1 / angka2;
            }
        }
    }

    document.getElementById("hasil").innerHTML = hasil;
}