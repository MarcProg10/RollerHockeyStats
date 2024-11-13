package com.marc.rollerhockeystats.ui.creatematch.ui

//import androidx.compose.foundation.layout.Row
//import androidx.compose.material3.TextField
//
//import android.util.Log
//import androidx.compose.foundation.Image
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Column
//
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.size
//import androidx.compose.foundation.layout.width
//import androidx.compose.material3.Button
//import androidx.compose.material3.Scaffold
//import androidx.compose.material3.Text
//import androidx.compose.material3.TopAppBar
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.semantics.Role.Companion.Image
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import com.marc.rollerhockeystats.R
//import org.intellij.lang.annotations.JdkConstants.HorizontalAlignment
//
//@Composable
//fun createMatchScreen(){
//
//    Row {
//
//    }
//
//}
//
//@Preview(showSystemUi = true)
//@Composable
//fun previewCreateMatchScreen(){
//    createMatchScreen()
//}

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class Player(val name: String = "", val number: String = "")

@Composable
fun CreateMatchScreen(){

}


@Preview(showSystemUi = true, showBackground = true)
@Composable
fun CreateMatchPreview(){
    CreateMatchScreen()
}
