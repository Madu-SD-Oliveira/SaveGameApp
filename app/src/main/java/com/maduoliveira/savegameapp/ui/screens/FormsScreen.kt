package com.maduoliveira.savegameapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.maduoliveira.savegameapp.R
import com.maduoliveira.savegameapp.ui.screens.forms.FormsUiState
import com.maduoliveira.savegameapp.ui.theme.TypographySaveGame

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormsScreen(
    uiState: FormsUiState,
    onDescriptionChange: (String) -> Unit,
    onNameChange: (String) -> Unit,
    onValueChange: (String) -> Unit,
    onTypeChange: (String) -> Unit,
    onDateChange: (String) -> Unit,
    onCategorySelect: (String) -> Unit,
    onSectorSelect: (String) -> Unit,
    onChannelSelect: (String) -> Unit,
    onAccountSelect: (String) -> Unit,
    onSaveClick: () -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    LaunchedEffect(uiState.isTransactionSaved) {
        if (uiState.isTransactionSaved) {
            onBackClick()
        }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        // HEADER
        item(key = "title_forms") {
            Text(
                text = stringResource(R.string.new_input),
                color = MaterialTheme.colorScheme.onBackground,
                style = TypographySaveGame.headlineMedium,
                modifier = Modifier
                    .padding(top = 16.dp)
            )
        }

        // Type selector: INCOME / EXPENSE
        item(key = "card_type_selector") {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = CardDefaults.shape,
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Column(
                        Modifier
                            .padding(4.dp),
                        verticalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Text(
                            text = stringResource(R.string.type_of_transaction),
                            color = MaterialTheme.colorScheme.onBackground,
                            style = TypographySaveGame.titleMedium,
                            modifier = Modifier
                                .padding(bottom = 8.dp)
                        )
                        Row(modifier = Modifier.fillMaxWidth()) {
                            Button(
                                onClick = { onTypeChange("INCOMES") },
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (uiState.type == "INCOMES") {
                                        MaterialTheme.colorScheme.primary
                                    } else {
                                        MaterialTheme.colorScheme.surfaceVariant
                                    },
                                    contentColor = if (uiState.type == "INCOMES") {
                                        MaterialTheme.colorScheme.onPrimary
                                    } else {
                                        MaterialTheme.colorScheme.onSurfaceVariant
                                    }
                                )
                            ) {
                                Text(stringResource(R.string.income))
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Button(
                                onClick = { onTypeChange("EXPENSES") },
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (uiState.type == "EXPENSES") {
                                        MaterialTheme.colorScheme.secondary
                                    } else {
                                        MaterialTheme.colorScheme.surfaceVariant
                                    },
                                    contentColor = if (uiState.type == "EXPENSES") {
                                        MaterialTheme.colorScheme.onSecondary
                                    } else {
                                        MaterialTheme.colorScheme.onSurfaceVariant
                                    }
                                )
                            ) {
                                Text(stringResource(R.string.expense))
                            }
                        }
                    }
                }
            }
        }

        // CAMPO: Name
        item(key = "field_name") {
            OutlinedTextField(
                value = uiState.name,
                onValueChange = onNameChange,
                label = { Text(stringResource(R.string.name)) },
                placeholder = { Text(stringResource(R.string.title_example)) },
                keyboardOptions = KeyboardOptions(showKeyboardOnFocus = true),
                shape = OutlinedTextFieldDefaults.shape,
                trailingIcon = { Icon(ImageVector.vectorResource(id = R.drawable.ic_title), contentDescription = null) },
                modifier = Modifier.fillMaxWidth()
            )
        }
        // CAMPO: Transaction value
        item(key = "field_value") {
            OutlinedTextField(
                value = uiState.value,
                onValueChange = onValueChange,
                label = { Text(stringResource(R.string.value_rs)) },
                placeholder = { Text(stringResource(R.string._0_00)) },
                isError = uiState.valueError != null,
                supportingText = { uiState.valueError?.let { Text(it) } },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                shape = OutlinedTextFieldDefaults.shape,
                trailingIcon = { Icon(ImageVector.vectorResource(id = R.drawable.ic_coin), contentDescription = null) },
                modifier = Modifier.fillMaxWidth()
            )
        }

        // CAMPO: Transaction data
        item(key = "field_date") {
            OutlinedTextField(
                value = uiState.dateText,
                onValueChange = onDateChange,
                label = { Text(stringResource(R.string.date)) },
                placeholder = { Text(stringResource(R.string.date_placeholder)) },
                isError = uiState.dateError != null,
                supportingText = { uiState.dateError?.let { Text(it) } },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                trailingIcon = { Icon(ImageVector.vectorResource(id = R.drawable.calendar), contentDescription = null) },
                modifier = Modifier.fillMaxWidth()
            )
        }

        // CAMPO: Item Description
        item(key = "field_description") {
            OutlinedTextField(
                value = uiState.description,
                onValueChange = onDescriptionChange,
                label = { Text(stringResource(R.string.description_optional)) },
                placeholder = { Text(stringResource(R.string.ex_description)) },
                trailingIcon = { Icon(ImageVector.vectorResource(id = R.drawable.ic_description), contentDescription = null) },
                modifier = Modifier.fillMaxWidth()
            )
        }

        item(key = "field_sector"){
            var sectorExpanded by remember { mutableStateOf(false) }
            ExposedDropdownMenuBox(
                expanded = sectorExpanded,
                onExpandedChange = { sectorExpanded = !sectorExpanded }
            ) {
                OutlinedTextField(
                    value = uiState.selectedSector.ifBlank { stringResource(R.string.choose_a_sector) },
                    onValueChange = {},
                    readOnly = true,
                    label = { Text(stringResource(R.string.sector)) },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = sectorExpanded) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                )
                ExposedDropdownMenu(
                    expanded = sectorExpanded,
                    onDismissRequest = { sectorExpanded = false }
                ) {
                    uiState.sectorsAvailable.forEach { sector ->
                        DropdownMenuItem(
                            text = { Text(sector) },
                            onClick = {
                                onSectorSelect(sector)
                                sectorExpanded = false
                            }
                        )
                    }
                }
            }
        }

        // MENU SELECT: CATEGORIA / SETOR
        item(key = "dropdown_category_type") {
            if (uiState.type == "INCOMES") {
                var channelExpanded by remember { mutableStateOf(false) }
                ExposedDropdownMenuBox(
                    expanded = channelExpanded,
                    onExpandedChange = { channelExpanded = !channelExpanded }
                ) {
                    OutlinedTextField(
                        value = uiState.selectedChannel.ifBlank { stringResource(R.string.choose_a_channel) },
                        onValueChange = {},
                        readOnly = true,
                        label = { Text(stringResource(R.string.type)) },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = channelExpanded) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = channelExpanded,
                        onDismissRequest = { channelExpanded = false }
                    ) {
                        uiState.channelsAvailable.forEach { sector ->
                            DropdownMenuItem(
                                text = { Text(sector) },
                                onClick = {
                                    onChannelSelect(sector)
                                    channelExpanded = false
                                }
                            )
                        }
                    }
                }
            } else {
                var categoryExpanded by remember { mutableStateOf(false) }
                ExposedDropdownMenuBox(
                    expanded = categoryExpanded,
                    onExpandedChange = { categoryExpanded = !categoryExpanded }
                ) {
                    OutlinedTextField(
                        value = uiState.selectedCategory.ifBlank { stringResource(R.string.choose_a_category) },
                        onValueChange = {},
                        readOnly = true,
                        label = { Text(stringResource(R.string.category)) },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = categoryExpanded) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = categoryExpanded,
                        onDismissRequest = { categoryExpanded = false }
                    ) {
                        uiState.categoriesAvailable.forEach { category ->
                            DropdownMenuItem(
                                text = { Text(category) },
                                onClick = {
                                    onCategorySelect(category)
                                    categoryExpanded = false
                                }
                            )
                        }
                    }
                }
            }
        }

        // MENU SELECT: Account
        item(key = "dropdown_account") {
            var accountExpanded by remember { mutableStateOf(false) }
            ExposedDropdownMenuBox(
                expanded = accountExpanded,
                onExpandedChange = { accountExpanded = !accountExpanded }
            ) {
                OutlinedTextField(
                    value = uiState.selectedAccount.ifBlank { stringResource(R.string.choose_an_account) },
                    onValueChange = {},
                    readOnly = true,
                    label = { Text(stringResource(R.string.conta_de_destino_origem)) },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = accountExpanded) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                )
                ExposedDropdownMenu(
                    expanded = accountExpanded,
                    onDismissRequest = { accountExpanded = false }
                ) {
                    uiState.accountsAvailable.forEach { account ->
                        DropdownMenuItem(
                            text = { Text(account) },
                            onClick = {
                                onAccountSelect(account)
                                accountExpanded = false
                            }
                        )
                    }
                }
            }
        }

        // SAVE INPUT BUTTON
        item(key = "button_save") {
            Spacer(modifier = Modifier.size(16.dp))
            Button(
                onClick = onSaveClick,
                enabled = !uiState.isLoading,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiary)
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.onTertiary,
                        modifier = Modifier.size(24.dp)
                    )
                } else {
                    Text(stringResource(R.string.concluir_mission), style = TypographySaveGame.labelMedium)
                }
            }
        }
    }
}
