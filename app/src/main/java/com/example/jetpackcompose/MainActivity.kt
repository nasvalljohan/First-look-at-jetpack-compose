package com.example.jetpackcompose

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.jetpackcompose.ui.theme.JetpackComposeTheme
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.constraintlayout.compose.ChainStyle
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.Dimension
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val painter = painterResource(id = R.drawable.hs)
            val description = "Picture of Munk"
            val title = "Look, Munk is happy!!"

            val constraints = ConstraintSet{
                val greenBox = createRefFor("greenBox")
                val redBox = createRefFor("redBox")
                val guideline = createGuidelineFromTop(0.1f) // Invis guideline for greenBox

                constrain(greenBox){
                    top.linkTo(guideline)
                    start.linkTo(parent.start)
                    width = Dimension.value(100.dp)
                    height = Dimension.value(100.dp)
                }
                constrain(redBox){
                    top.linkTo(parent.top)
                    end.linkTo(parent.end)
                    width = Dimension.value(100.dp)
                    height = Dimension.value(100.dp)
                }
                createHorizontalChain(greenBox, redBox, chainStyle = ChainStyle.Packed)
            }


            Column {
                ConstraintLayout(constraints, modifier = Modifier.fillMaxWidth()) {
                    Box(modifier = Modifier
                        .background(Color.Green)
                        .layoutId("greenBox")
                    )
                    Box(modifier = Modifier
                        .background(Color.Red)
                        .layoutId("redBox")
                    )
                }
                Column{
                    Box(modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.2f)){
                        ScaffoldCustom(scaffoldState = rememberScaffoldState())
                    }

                    Box(modifier = Modifier
                        .fillMaxWidth(0.5f)
                        .padding(5.dp)
                    ){
                        ImageCard(title = title, contentDescription = description, painter = painter)
                    }

                }

                LazyColumn {
                    itemsIndexed(
                        listOf("This", "is", "jetpack", "compose", "and", "i", "want", "to", "say", "hi")
                    ){index, string ->
                        Text(text= "$string $index",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 24.dp)
                        )
                    }

                }
            }

        }
    }
}

@Composable
fun ScaffoldCustom(
    scaffoldState: ScaffoldState,
){
    var textFieldState by remember {
        mutableStateOf("")
    }

    Scaffold(modifier = Modifier
        .fillMaxSize(),
        scaffoldState = scaffoldState,
        contentColor = Color.Black,
        content = { padding ->
            Column(horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                TextField(
                    value = textFieldState,
                    label = {
                        Text("Enter your name")
                    },
                    onValueChange = {
                        textFieldState = it
                    },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                ButtonCard(scaffoldState = scaffoldState, textFieldState = textFieldState)
            }

        }
    )
}

@Composable
fun ButtonCard(
    scaffoldState: ScaffoldState,
textFieldState: String
){
    val scope = rememberCoroutineScope()
    Button(modifier = Modifier
        .fillMaxWidth(),
        onClick = {
            scope.launch {
                scaffoldState.snackbarHostState.showSnackbar("Hello $textFieldState")
            }

        }) {
        Text("click me")
    }
}



@Composable
fun ImageCard(
    title: String,
    contentDescription: String,
    modifier: Modifier = Modifier,
    painter: Painter
) {
    var color by remember {
        mutableStateOf(Color.Yellow)
    }
    var color1 by remember {
        mutableStateOf(Color.Yellow)
    }

    val fontFamily = FontFamily(
        Font(R.font.unbounded_extralight, FontWeight.ExtraLight),
        Font(R.font.unbounded_light, FontWeight.Light),
        Font(R.font.unbounded_medium, FontWeight.Medium),
        Font(R.font.unbounded_regular, FontWeight.Thin),
        Font(R.font.unbounded_semibold, FontWeight.SemiBold),
        Font(R.font.unbounded_bold, FontWeight.Bold),
        Font(R.font.unbounded_extrabold, FontWeight.ExtraBold),
        Font(R.font.unbounded_black, FontWeight.Black)
    )

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable {
                color = Color(
                    Random.nextFloat(),
                    Random.nextFloat(),
                    Random.nextFloat(),
                    1f,
                )
                color1 = Color(
                    Random.nextFloat(),
                    Random.nextFloat(),
                    Random.nextFloat(),
                    1f,
                )
            },
        shape = RoundedCornerShape(10.dp),
        elevation = 8.dp
    ) {
        Box(modifier = Modifier.height(200.dp)){
            Image(
                painter = painter,
                contentDescription = contentDescription,
                contentScale = ContentScale.Crop,
                )
            Box(modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.Black,
                        ), startY = 300f
                    )
                ))
            Box(modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
                contentAlignment = Alignment.BottomCenter){
                Text(
                    text = buildAnnotatedString {
                                                withStyle(
                                                    style = SpanStyle(
                                                        color = color,
                                                        fontSize = 15.sp,
                                                        fontFamily = fontFamily,
                                                        fontWeight = FontWeight.ExtraBold
                                                    )
                                                ){
                                                    append("M")
                                                }
                        append("aja the ")
                        withStyle(
                            style = SpanStyle(
                                color = color1,
                                fontSize = 15.sp,
                                fontFamily = fontFamily,
                                fontWeight = FontWeight.ExtraBold
                            )
                        ){
                            append("M")
                        }
                        append("unk")
                    },
                    style = TextStyle(color = Color.White, fontSize = 16.sp),
                    color = Color.White,
                    fontFamily = fontFamily,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Thin,
                )
            }
        }
    }
}




@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    JetpackComposeTheme {
    }
}