package com.example.upp_app.ui.screen.chatbot

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.upp_app.R
import com.example.upp_app.ui.theme.BorderColor
import com.example.upp_app.ui.theme.NeutralPrimary
import com.example.upp_app.ui.utils.localizedString
import kotlinx.coroutines.delay

data class ChatMessage(
    val id: String,
    val message: String,
    val isFromUser: Boolean,
    val timestamp: Long = System.currentTimeMillis()
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatbotScreen(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    // Estado para el mensaje que se está escribiendo
    var messageText by remember { mutableStateOf("") }

    // Lista de mensajes en el chat
    val messages = remember { mutableStateListOf<ChatMessage>() }

    // Estado para mostrar el indicador de "escribiendo..."
    var isTyping by remember { mutableStateOf(false) }

    // Estado para la lista de mensajes
    val listState = rememberLazyListState()

    // Función para enviar un mensaje
    fun sendMessage(text: String) {
        if (text.isNotBlank()) {
            // Agregar mensaje del usuario
            messages.add(
                ChatMessage(
                    id = System.currentTimeMillis().toString(),
                    message = text,
                    isFromUser = true
                )
            )

            // Limpiar el campo de texto
            messageText = ""

            // Mostrar indicador de "escribiendo..."
            isTyping = true
        }
    }

    // Efecto para simular la respuesta del chatbot
    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty() && messages.last().isFromUser) {
            delay(1500) // Simular tiempo de respuesta

            // Ocultar indicador de "escribiendo..."
            isTyping = false

            // Agregar respuesta del chatbot
            val lastMessage = messages.last().message
            val response = when {
                lastMessage.contains("hello", ignoreCase = true) -> "Hello! How can I help you today?"
                lastMessage.contains("help", ignoreCase = true) ->
                    "I can help with a wide variety of tasks! Here are some examples of what you can tell me to do:\n\n" +
                            "1. Creative Writing:\n" +
                            "   • Write a short story or a poem.\n" +
                            "   • Brainstorm ideas for a novel, script, or essay.\n" +
                            "   • Generate plot twists or character ideas for your story.\n\n" +
                            "2. Information and Research:\n" +
                            "   • Provide summaries of topics (e.g., scientific theories, historical events).\n" +
                            "   • Explain complex concepts in simple terms.\n" +
                            "   • Answer questions about various subjects."
                lastMessage.contains("thanks", ignoreCase = true) -> "You're welcome! Is there anything else I can help with?"
                else -> "I understand you said: \"${lastMessage}\". How can I assist you with that?"
            }

            messages.add(
                ChatMessage(
                    id = System.currentTimeMillis().toString(),
                    message = response,
                    isFromUser = false
                )
            )
        }
    }

    // Efecto para desplazar al último mensaje
    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Assistant",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Medium
                            ),
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        Icon(
                            painter = painterResource(id = R.drawable.ic_arrow_right),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                            modifier = Modifier.size(16.dp)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                ),
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_arrow_back),
                            contentDescription = "Back",
                            tint = Color.Black
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { /* Implementar acción de compartir */ }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_edit),
                            contentDescription = "New Chat",
                            tint = Color.Black
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(Color(0xFFF7F7F8))
                .padding(paddingValues)
                .systemBarsPadding(),
        ) {
            // Highlight pill at the top
            if (messages.isEmpty()) {
                Box(
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .align(Alignment.CenterHorizontally)
                ) {
                    Surface(
                        color = Color(0xFFE0E0E0),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "Highlight",
                            color = Color.DarkGray,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                        )
                    }
                }
            }

            // Área de mensajes
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                state = listState,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Mensaje de bienvenida inicial
                if (messages.isEmpty()) {
                    item {
                        Spacer(modifier = Modifier.height(16.dp))

                        // User's initial question
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(end = 64.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .background(Color(0xFFE9E9EB))
                                .padding(16.dp)
                        ) {
                            Text(
                                text = "What can I tell you to do? Give examples, then ask me how I want to start.",
                                color = Color.Black
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // AI's response
                        ChatBubble(
                            message = ChatMessage(
                                id = "welcome",
                                message = "I can help with a wide variety of tasks! Here are some examples of what you can tell me to do:\n\n" +
                                        "1. Creative Writing:\n" +
                                        "   • Write a short story or a poem.\n" +
                                        "   • Brainstorm ideas for a novel, script, or essay.\n" +
                                        "   • Generate plot twists or character ideas for your story.\n\n" +
                                        "2. Information and Research:\n" +
                                        "   • Provide summaries of topics (e.g., scientific theories, historical events).\n" +
                                        "   • Explain complex concepts in simple terms.\n" +
                                        "   • Answer questions about various subjects.",
                                isFromUser = false
                            )
                        )
                    }
                } else {
                    items(messages) { message ->
                        ChatBubble(message = message)
                    }
                }

                // Indicador de "escribiendo..."
                if (isTyping) {
                    item {
                        TypingIndicator()
                    }
                }

                // Espacio al final para que el último mensaje sea visible
                item {
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

            // Área de entrada de mensaje
            Surface(
                color = Color.White,
                shadowElevation = 4.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Attachment buttons
                    IconButton(
                        onClick = { /* Implementar adjuntar imagen */ },
                        modifier = Modifier.size(40.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_chat),
                            contentDescription = "Attach Image",
                            tint = Color.Gray,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    IconButton(
                        onClick = { /* Implementar adjuntar archivo */ },
                        modifier = Modifier.size(40.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_data),
                            contentDescription = "Attach File",
                            tint = Color.Gray,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    IconButton(
                        onClick = { /* Implementar adjuntar documento */ },
                        modifier = Modifier.size(40.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_calendar),
                            contentDescription = "Attach Document",
                            tint = Color.Gray,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    // Message input field
                    OutlinedTextField(
                        value = messageText,
                        onValueChange = { messageText = it },
                        placeholder = {
                            Text(
                                "Message",
                                color = Color.Gray
                            )
                        },
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 8.dp),
                        shape = RoundedCornerShape(24.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedBorderColor = BorderColor,
                            focusedBorderColor = NeutralPrimary,
                            unfocusedContainerColor = Color(0xFFF7F7F8),
                            focusedContainerColor = Color(0xFFF7F7F8)
                        ),
                        maxLines = 1
                    )

                    // Send button
                    IconButton(
                        onClick = { sendMessage(messageText) },
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(Color.Black)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_send),
                            contentDescription = "Send",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ChatBubble(message: ChatMessage) {
    val alignment = if (message.isFromUser) Alignment.End else Alignment.Start
    val backgroundColor = if (message.isFromUser) Color(0xFFE9E9EB) else Color.White
    val textColor = Color.Black
    val bubbleShape = RoundedCornerShape(16.dp)
    val bubbleModifier = if (message.isFromUser) {
        Modifier.padding(start = 64.dp)
    } else {
        Modifier.padding(end = 64.dp)
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = alignment
    ) {
        Box(
            modifier = Modifier
                .then(bubbleModifier)
                .clip(bubbleShape)
                .background(backgroundColor)
                .padding(16.dp)
        ) {
            Text(
                text = message.message,
                color = textColor
            )
        }
    }
}

@Composable
fun TypingIndicator() {
    Row(
        modifier = Modifier
            .padding(8.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
            .padding(12.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(3) { index ->
            TypingDot(delayMillis = index * 300L)
        }
    }
}

@Composable
fun TypingDot(delayMillis: Long) {
    val infiniteTransition = rememberInfiniteTransition(label = "typing")
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.2f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(600, delayMillis.toInt(), LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "alpha"
    )

    Box(
        modifier = Modifier
            .size(8.dp)
            .clip(CircleShape)
            .background(NeutralPrimary.copy(alpha = alpha))
    )
}

