# Pac-Man 
Tugas mata kuliah Pemrograman Berorientasi Objek (OOP)

Pac-Man adalah game 2D yang terinspirasi dari game klasik Pac-Man. Game ini dikembangkan dengan mengimplementasikkan konsep Object-Oriented Programming (OOP). Dalam game ini, pemain harus mengumpulkan seluruh makanan di dalam labirin sambil menghindari hantu dengan jumlah nyawa yang terbatas.

## Preview
<table>
  <tr>
    <td align="center">
      <img src="src/assets/images/Main_Menu.png" width="300"><br>
      <b>Main Menu</b>
    </td>
  </tr>
  <tr>
    <td align="center">
      <img src="src/assets/images/Gameplay.png" width="300"><br>
      <b>Gameplay</b>
    </td>
  </tr>
  <tr>
    <td align="center">
      <img src="src/assets/images/Menu_Pause.png" width="300"><br>
      <b>Pause Menu</b>
    </td>
  </tr>
  <tr>
    <td align="center">
      <img src="src/assets/images/Gameover.png" width="300"><br>
      <b>Game Over</b>
    </td>
  </tr>
  <tr>
    <td align="center">
      <img src="src/assets/images/Win.png" width="300"><br>
      <b>Win</b>
    </td>
  </tr>
</table>

## Fitur
- Menu, Pause, Game Over, dan Win screen
- Player movement menggunakan keyboard (WASD / Arrow Keys)
- Esc untuk pause game
- Gerak pacman & hantu berbasis tile map
- Tunnel wrap-around yang tembus dari sisi kiri map ke kanan (dan sebaliknya) lewat celah kosong (O) di map.
- Animasi sprit. Pacman (4 arah, dari sprite sheet) dan hantu (looping, 4 warna: kuning, oranye, pink, merah).
- Collectible food. Render pakai gambar
- Lives system. Ikon Pac-Man
- Header skor. 
- Score system + high score yang tersimpan permanen (`highscore.txt`)
- Collision detection antara Pac-Man, Ghost, dan Food



## Struktur Project
<table>
  <thead>
    <tr>
      <th>Direktori / File</th>
      <th>Deskripsi</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td><code>assets/</code></td>
      <td>Berisi seluruh aset game, seperti sprite, peta, antarmuka (UI), dan font.</td>
    </tr>
    <tr>
      <td><code>Main.java</code></td>
      <td>Entry point aplikasi yang menjalankan game dan membuka jendela utama.</td>
    </tr>
    <tr>
      <td><code>GameBoard.java</code></td>
      <td>Mengelola game loop, menggambar tampilan game, mendeteksi tabrakan, dan membaca input keyboard.</td>
    </tr>
    <tr>
      <td><code>GameState.java</code></td>
      <td>Mengelola perpindahan state permainan, seperti Menu, Playing, Pause, Win, dan Game Over.</td>
    </tr>
    <tr>
      <td><code>GameMap.java</code></td>
      <td>Membuat layout labirin dan objek permainan berdasarkan data peta.</td>
    </tr>
    <tr>
      <td><code>GameImages.java</code></td>
      <td>Memuat seluruh aset gambar, seperti sprite sheet dan tile.</td>
    </tr>
    <tr>
      <td><code>Player.java</code></td>
      <td>Mengelola karakter Pac-Man, termasuk pergerakan, animasi, dan interaksi dengan objek lain.</td>
    </tr>
    <tr>
      <td><code>Ghost.java</code></td>
      <td>Mengelola perilaku hantu, termasuk pergerakan dan animasinya.</td>
    </tr>
    <tr>
      <td><code>Food.java</code></td>
      <td>Mengelola makanan yang dapat dikumpulkan pemain untuk menambah skor.</td>
    </tr>
    <tr>
      <td><code>Wall.java</code></td>
      <td>Mengelola tembok atau dinding sebagai rintangan pada labirin.</td>
    </tr>
    <tr>
      <td><code>SpriteAnimation.java</code></td>
      <td>Mengelola pergantian frame animasi pada karakter dan objek permainan.</td>
    </tr>
    <tr>
      <td><code>SpriteSheet.java</code></td>
      <td>Memproses sprite sheet menjadi kumpulan frame untuk animasi.</td>
    </tr>
    <tr>
      <td><code>UiButton.java</code></td>
      <td>Mengelola komponen tombol pada antarmuka pengguna.</td>
    </tr>
    <tr>
      <td><code>HighScoreStorage.java</code></td>
      <td>Menyimpan dan memuat data skor tertinggi ke dalam file.</td>
    </tr>
    <tr>
      <td><code>Block.java</code></td>
      <td>Kelas dasar yang menyimpan atribut umum objek permainan, seperti posisi, ukuran, dan deteksi tabrakan.</td>
    </tr>
    <tr>
      <td><code>README.md</code></td>
      <td>Dokumentasi project yang berisi informasi mengenai game, cara menjalankan, struktur project, dan sumber aset.</td>
    </tr>
  </tbody>
</table>


## Cara Menjalankan
### Opsi 1 — Run File Executable (.jar)
1. Download atau clone repository ini.
2. Buka folder project.
3. Jalankan file **`Pac-Man.jar`** dengan melakukan double-click.
4. Game akan terbuka secara otomatis.

### Opsi 2 — Run dari Source Code
1. Clone repository
```bash
git clone https://github.com/ithaapy/Pac-Man.git
```
2. Buka project menggunakan IDE seperti VS Code, IntelliJ IDEA, atau NetBeans.
3. Compile project.
4. Jalankan `Main.java`.

## Kontrol
<table>
  <thead>
    <tr>
      <th>Key</th>
      <th>Action</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td><kbd>↑</kbd> / <kbd>W</kbd></td>
      <td>Move Up</td>
    </tr>
    <tr>
      <td><kbd>↓</kbd> / <kbd>S</kbd></td>
      <td>Move Down</td>
    </tr>
    <tr>
      <td><kbd>←</kbd> / <kbd>A</kbd></td>
      <td>Move Left</td>
    </tr>
    <tr>
      <td><kbd>→</kbd> / <kbd>D</kbd></td>
      <td>Move Right</td>
    </tr>
    <tr>
      <td><kbd>Esc</kbd></td>
      <td>Pause / Resume</td>
    </tr>
  </tbody>
</table>


## Asset
- Pac-Man Practice Assets by CheckpointCafé.Dev  
  https://checkpointcafe.itch.io/pacman-practice-assets  
  *Sprites: Pac-Man, Ghosts, Maze Tiles*
- Pixel UI Button Icon Platformer by BDragon1727  
  https://bdragon1727.itch.io/pixel-ui-button-icon-platformer  
  *UI Button Assets*
- GUI Buttons by Nam Dinh  
  https://namdinh.itch.io/gui-buttons  
  *Additional GUI Button Assets*
- Pac-Man Java by ImKennyYip  
  https://github.com/ImKennyYip/pacman-java  
  *Wall sprite (PNG), Power Food (PNG), and programming reference*
- DePixel Halbfett by Ingo Zimmermann (ingoFonts)  
  https://www.ingofonts.com/  
  *Font: DePixelHalbfett.ttf


## Lisensi
Proyek ini dibuat hanya untuk tujuan edukasi.
This project was developed for educational purposes only.







