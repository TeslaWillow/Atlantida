// ./src/services/accountService.ts
import { axiosClient } from '../api/axiosClient';
import type { Account } from '../types/banking';

export const accountService = {
  async getAll(): Promise<Account[]> {
    const response = await axiosClient.get<Account[]>('/accounts');
    return response.data;
  },

  async getById(id: number): Promise<Account> {
    const response = await axiosClient.get<Account>(`/accounts/${id}`);
    return response.data;
  }
};