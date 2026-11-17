// ./src/types/banking.ts
export interface Account {
  id: number;
  holderName: string;
  balance: number;
}

export interface TransferRequest {
  sourceAccountId: number;
  destinationAccountId: number;
  amount: number;
}

export interface TransferResponse {
  transferId: number;
  sourceAccountId: number;
  sourceHolderName: string;
  sourcePreviousBalance: number;
  sourceNewBalance: number;
  destinationAccountId: number;
  destinationHolderName: string;
  destinationPreviousBalance: number;
  destinationNewBalance: number;
  amount: number;
  createdAt: string;
}