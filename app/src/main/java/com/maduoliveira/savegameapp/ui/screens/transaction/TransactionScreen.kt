package com.maduoliveira.savegameapp.ui.screens.transaction

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.InputChip
import androidx.compose.material3.InputChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.maduoliveira.savegameapp.R
import com.maduoliveira.savegameapp.domain.transaction.TransactionUIModel
import com.maduoliveira.savegameapp.domain.transaction.TransactionsViewModel
import com.maduoliveira.savegameapp.ui.screens.dashboard.customCardShape
import com.maduoliveira.savegameapp.ui.theme.TypographySaveGame

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionsScreen(
    viewModel: TransactionsViewModel = viewModel(),
    onEditTransaction: (Long) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var transactionPendingDelete by remember { mutableStateOf<TransactionUIModel?>(null) }
    var expandedTransactionId by remember { mutableStateOf<Long?>(null) }

    if (transactionPendingDelete != null) {
        AlertDialog(
            onDismissRequest = { transactionPendingDelete = null },
            title = { Text(stringResource(R.string.excluir_mission), style = TypographySaveGame.titleMedium) },
            text = {
                Text(
                    stringResource(
                        R.string.delete_confirmation,
                        transactionPendingDelete?.transaction?.name ?: ""
                    )
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        transactionPendingDelete?.let { viewModel.deleteTransaction(it.transaction) }
                        transactionPendingDelete = null
                    }
                ) {
                    Text(stringResource(R.string.delete), color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { transactionPendingDelete = null }) {
                    Text(stringResource(R.string.cancel))
                }
            }
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Header Area
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            AnimatedVisibility(
                visible = !uiState.isSearchExpanded,
                enter = fadeIn(),
                exit = fadeOut(),
                modifier = Modifier.wrapContentSize()
            ) {
                Text(
                    text = stringResource(R.string.transactions),
                    style = TypographySaveGame.headlineMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }

            AnimatedVisibility(
                visible = uiState.isSearchExpanded,
                enter = fadeIn() + expandHorizontally(expandFrom = Alignment.End),
                exit = fadeOut() + shrinkHorizontally(shrinkTowards = Alignment.End),
                modifier = Modifier
                    .weight(if (uiState.isSearchExpanded) 1f else 0.0001f)
                    .padding(end = if (uiState.isSearchExpanded) 8.dp else 0.dp)
            ) {
                OutlinedTextField(
                    value = uiState.searchQuery,
                    onValueChange = { viewModel.onSearchQueryChange(it) },
                    placeholder = { Text(stringResource(R.string.search)) },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                    trailingIcon = {
                        IconButton(onClick = { viewModel.onSearchExpandedChange(false) }) {
                            Icon(Icons.Default.Clear, contentDescription = stringResource(R.string.fechar_busca))
                        }
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                )
            }

            AnimatedVisibility(
                visible = !uiState.isSearchExpanded,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                IconButton(
                    onClick = { viewModel.onSearchExpandedChange(true) },
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Icon(
                        Icons.Default.Search,
                        contentDescription = stringResource(R.string.expand_search),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        // Filters Area
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf(
                        stringResource(R.string.all),
                        stringResource(R.string.one_month),
                        stringResource(R.string.three_months)
                    ).zip(listOf("All", "1 Month", "3 Month")).forEach { (label, value) ->
                        val isSelected = uiState.selectedPeriod == value
                        InputChip(
                            selected = isSelected,
                            onClick = { viewModel.onPeriodSelect(value) },
                            label = { Text(label) },
                            shape = customCardShape,
                            colors = InputChipDefaults.inputChipColors(
                                selectedContainerColor = MaterialTheme.colorScheme.tertiary,
                                selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                            )
                        )
                    }
                }

                IconButton(
                    onClick = { viewModel.toggleOrder() },
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                    modifier = Modifier.clip(customCardShape)
                ) {
                    Icon(
                        imageVector = if (uiState.isAscending) {
                            ImageVector.vectorResource(id = R.drawable.ic_arrow_up)
                        } else {
                            ImageVector.vectorResource(id = R.drawable.ic_arrow_down)
                        },
                        contentDescription = stringResource(R.string.ordenacao),
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf(
                    stringResource(R.string.all),
                    stringResource(R.string.incomes),
                    stringResource(R.string.expenses)
                ).zip(listOf("ALL", "INCOMES", "EXPENSES")).forEach { (label, value) ->
                    val isSelected = uiState.selectedType == value
                    val containerColor = when {
                        !isSelected -> MaterialTheme.colorScheme.surface
                        value == "INCOMES" -> MaterialTheme.colorScheme.primary
                        value == "EXPENSES"  -> MaterialTheme.colorScheme.error
                        else -> MaterialTheme.colorScheme.onSurface
                    }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(customCardShape)
                            .background(containerColor)
                            .clickable { viewModel.onTypeSelect(value) }
                            .padding(vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = label,
                            style = TypographySaveGame.labelSmall,
                            color = if (isSelected) MaterialTheme.colorScheme.onBackground else MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }

            // Category Filter
            /*AnimatedVisibility(visible = uiState.selectedType == "ALL" || uiState.selectedType == "EXPENSES") {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        stringResource(R.string.category),
                        style = TypographySaveGame.bodySmall,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                    )

                    Box(modifier = Modifier.weight(1f)) {
                        val scrollState = rememberScrollState()
                        Row(
                            modifier = Modifier.horizontalScroll(scrollState),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            val categoryOptions = listOf("All") + uiState.availableCategories
                            categoryOptions.forEach { name ->
                                val isSelected = uiState.selectedCategory == name
                                val label = if (name == "All") stringResource(R.string.all) else name

                                Text(
                                    text = label,
                                    style = TypographySaveGame.bodyMedium,
                                    color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground.copy(
                                        alpha = 0.5f
                                    ),
                                    modifier = Modifier
                                        .clickable { viewModel.onCategorySelect(name) }
                                        .padding(horizontal = 4.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }
                }
            }
            */
            // Channel Filter
            /*AnimatedVisibility(visible = uiState.selectedType == "ALL" || uiState.selectedType == "INCOMES") {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        stringResource(R.string.choose_a_channel),
                        style = TypographySaveGame.bodySmall,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                    )

                    Box(modifier = Modifier.weight(1f)) {
                        val scrollState = rememberScrollState()
                        Row(
                            modifier = Modifier.horizontalScroll(scrollState),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            val channelOptions = listOf("All") + uiState.availableChannels
                            channelOptions.forEach { name ->
                                val isSelected = uiState.selectedChannel == name
                                val label = if (name == "All") stringResource(R.string.all) else name

                                Text(
                                    text = label,
                                    style = TypographySaveGame.bodyMedium,
                                    color = if (isSelected) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.onBackground.copy(
                                        alpha = 0.5f
                                    ),
                                    modifier = Modifier
                                        .clickable { viewModel.onChannelSelect(name) }
                                        .padding(horizontal = 4.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }
                }
            }*/

            // Sector Filter
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    stringResource(R.string.sector),
                    style = TypographySaveGame.bodySmall,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                )

                Box(modifier = Modifier.weight(1f)) {
                    val scrollState = rememberScrollState()
                    Row(
                        modifier = Modifier.horizontalScroll(scrollState),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        val sectorOptions = listOf("All") + uiState.availableSectors
                        sectorOptions.forEach { name ->
                            val isSelected = uiState.selectedSector == name
                            val label = if (name == "All") stringResource(R.string.all) else name
                            
                            Text(
                                text = label,
                                style = TypographySaveGame.bodyMedium,
                                color = if (isSelected) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.onBackground.copy(
                                    alpha = 0.5f
                                ),
                                modifier = Modifier
                                    .clickable { viewModel.onSectorSelect(name) }
                                    .padding(horizontal = 4.dp, vertical = 2.dp)
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Transactions List
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 0.dp, vertical = 0.dp),
            verticalArrangement = Arrangement.spacedBy(0.dp)
        ) {
            itemsIndexed(
                items = uiState.transactions,
                key = { _, item -> item.transaction.id }
            ) { index, uiModel ->

                val isExpanded = expandedTransactionId == uiModel.transaction.id

                val dismissState = rememberSwipeToDismissBoxState(
                    confirmValueChange = { dismissValue ->
                        if (dismissValue == SwipeToDismissBoxValue.EndToStart) {
                            transactionPendingDelete = uiModel
                            true
                        } else false
                    }
                )

                LaunchedEffect(transactionPendingDelete) {
                    if (transactionPendingDelete == null) {
                        dismissState.reset()
                    }
                }

                SwipeToDismissBox(
                    state = dismissState,
                    enableDismissFromStartToEnd = false,
                    enableDismissFromEndToStart = true,
                    backgroundContent = {
                        val color = if (dismissState.dismissDirection == SwipeToDismissBoxValue.EndToStart) {
                            MaterialTheme.colorScheme.errorContainer
                        } else Color.Transparent

                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(RoundedCornerShape(0.dp))
                                .background(color)
                                .padding(horizontal = 24.dp),
                            contentAlignment = Alignment.CenterEnd
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = stringResource(R.string.delete),
                                tint = MaterialTheme.colorScheme.onErrorContainer
                            )
                        }
                    },
                    content = {
                        ModernTransactionCard(
                            uiModel = uiModel,
                            isExpanded = isExpanded,
                            index = index,
                            onEditClick = { onEditTransaction(uiModel.transaction.id) },
                            onClick = {
                                expandedTransactionId = if (isExpanded) null else uiModel.transaction.id
                            },
                            onLongClick = {
                                transactionPendingDelete = uiModel
                            }
                        )
                    }
                )
            }
        }
    }
}
