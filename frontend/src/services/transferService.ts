// ./src/services/transferService.ts
import { axiosClient } from '../api/axiosClient';
import type { TransferRequest, TransferResponse } from '../types/banking';

export const transferService = {
  async execute(request: TransferRequest): Promise<TransferResponse> {
    const response = await axiosClient.post<TransferResponse>('/transfers', request);
    return response.data;
  }
};