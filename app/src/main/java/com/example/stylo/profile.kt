import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import coil.compose.rememberImagePainter
import com.example.stylo.R
import com.example.stylo.TenorSansRegular
import com.example.stylo.ui.theme.miamaFontFamily
import java.util.jar.Attributes
import java.util.jar.Attributes.Name


@Composable
fun ProfileScreen() {
    var userName by remember { mutableStateOf("Angelica") } // State untuk menyimpan nama pengguna
    var profileImageUri by remember { mutableStateOf<Uri?>(null) } // State untuk menyimpan URI foto profil

    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        profileImageUri = uri // Update URI saat pengguna memilih foto
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF3EEEA))
            .verticalScroll(rememberScrollState()),
        //.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally // Center aligns all items horizontally
    ){
        Row (modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            Image(
                painter = painterResource(id = R.drawable.burger_icon),
                contentDescription = "Burger Icon",
                modifier = Modifier
                    .size(50.dp)
                    .fillMaxSize()
                    .clickable {  }
                    .padding(top = 10.dp),

                // .padding(start = 160.dp, top = 16.dp)
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(text = "Stylo",
                fontSize = 45.sp,
                color = Color(0xFF776B5D),
                fontFamily = miamaFontFamily,
                modifier = Modifier
                    .padding(end = 35.dp)
            )
            Spacer(modifier = Modifier.weight(1f))
        }
        Spacer(modifier = Modifier.height(150.dp))
        // Teks "Hi," dan nama pengguna
        Column(
            modifier = Modifier
                .fillMaxWidth() // Pastikan teks dapat rata kanan
                .padding(end = 50.dp), // Tambahkan padding untuk memberikan ruang
            horizontalAlignment = Alignment.End // Rata kanan
        ) {
            Text(
                text = "Hi,",
                fontSize = 40.sp,
                fontFamily = TenorSansRegular,
                fontWeight = FontWeight.SemiBold, // Ganti dengan Tenor Sans jika font tersedia
                color = Color.Black,
                textAlign = TextAlign.End, // Teks rata kanan
            )
            Text(
                text = userName,
                fontSize = 50.sp,
                fontFamily = TenorSansRegular,
                fontWeight = FontWeight.Bold, // Ganti dengan Tenor Sans jika font tersedia
                color = Color.Black,
                textAlign = TextAlign.End // Teks rata kanan
            )
        }

        Spacer(modifier = Modifier.height(100.dp))
    // Background Container
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF3EEEA)), // Background color F3EEEA
            contentAlignment = Alignment.Center
        ) {
       
            // Card for the Profile
            Card(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .wrapContentHeight(),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(10.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),

                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Label for the Username
                    Text(
                        text = "Username",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Gray, // Label color
                        modifier = Modifier.padding(bottom = 8.dp) // Spacing below the label
                    )

                    // Input for User Name
                    BasicTextField(
                        value = userName,
                        onValueChange = { userName = it },
                        textStyle = TextStyle(
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.Black,
                            textAlign = TextAlign.Center
                        ),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions.Default.copy(
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                // Hide keyboard or perform any other action on Done
                            },
                        ),
                        modifier = Modifier
                            .fillMaxWidth(0.8f)
                            .padding(horizontal = 16.dp)
                    )

                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

        }
        Spacer(modifier = Modifier.height(20.dp))
        // Background Container
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF3EEEA)), // Background color F3EEEA
            contentAlignment = Alignment.Center
        ) {

            // Card for the Profile
            Card(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .wrapContentHeight(),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(10.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    // Label for Email
                    Text(
                        text = "Email",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Gray,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    // Input for Email
                    var email by remember { mutableStateOf("") }
                    BasicTextField(
                        value = email,
                        onValueChange = { email = it }, // Memperbarui teks secara langsung
                        textStyle = TextStyle(
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            textAlign = TextAlign.Center
                        ),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions.Default.copy(
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                // Aksi jika tombol "Done" ditekann
                            }
                        ),
                        modifier = Modifier
                            .fillMaxWidth(0.8f)
                            .padding(horizontal = 16.dp)
                            .background(Color.White) // Tambahkan latar belakang untuk memudahkan debugging
                            .focusable(true) // Pastikan elemen ini bisa menerima fokus
                    )


                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }

        Spacer(modifier = Modifier.height(150.dp))
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween, // Memberikan jarak antara elemen atas dan bawah
            horizontalAlignment = Alignment.CenterHorizontally // Menyelaraskan elemen ke tengah secara horizontal
        ) {
            // Spacer untuk membuat bagian atas kosong
            Spacer(modifier = Modifier.weight(1f)) // Elemen pertama mengambil ruang kosong di atas

            // Tombol Log Out
            Button(
                onClick = { /*TODO: Forgot Password action*/ },
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFDD8560) // Warna tombol
                )
            ) {
                Text(
                    text = "Log Out",
                    fontSize = 20.sp, // Ubah ukuran teks tombol
                    fontFamily = TenorSansRegular,
                    color = Color.White
                )
            }

        }

    }
}

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    ProfileScreen()
}
