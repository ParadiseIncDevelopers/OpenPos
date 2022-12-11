package com.utilities.interfaces;

import com.wallet.WalletLogs;
import java.time.LocalDateTime;
import java.util.ArrayList;

public interface ITransactionFilterMakers
{
    ArrayList<WalletLogs> filterMinimumAmounts(double minimumAmount);
    ArrayList<WalletLogs> filterMaximumAmounts(double maximumAmount);
    ArrayList<WalletLogs> filterStartDate(LocalDateTime startDate);
    ArrayList<WalletLogs> filterFinalDate(LocalDateTime finalDate);
    ArrayList<WalletLogs> filterReceivedAmounts(boolean isReceived);
    ArrayList<WalletLogs> filterSentAmounts(boolean isSent);
}
