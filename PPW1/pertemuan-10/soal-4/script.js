let praktikan = prompt("Apakah Anda praktikan PPW1? (iya/tidak)");

if (praktikan === "iya") {
    let nama = prompt("Masukkan nama:");
    let nim = prompt("Masukkan NIM:");
    let angkatan = prompt("Masukkan angkatan:");

    document.getElementById("hasil").innerHTML = `
        <table class="table table-bordered table-striped">
            <tr>
                <th>Nama</th>
                <td>${nama}</td>
            </tr>
            <tr>
                <th>NIM</th>
                <td>${nim}</td>
            </tr>
            <tr>
                <th>Angkatan</th>
                <td>${angkatan}</td>
            </tr>
        </table>
    `;
} else {
    document.getElementById("hasil").innerHTML = `
        <div class="alert alert-danger">
            Anda bukan praktikan PPW1, Anda tidak boleh masuk!
        </div>
    `;
}