// ./src/features/AccountList.tsx
import React, { useEffect, useState } from 'react';
import { accountService } from '../services/accountService';
import type { Account } from '../types/banking';

interface AccountListProps {
  onNavigateToTransfer: () => void;
}

export const AccountList: React.FC<AccountListProps> = ({ onNavigateToTransfer }) => {
  const [accounts, setAccounts] = useState<Account[]>([]);
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);

  const fetchAccounts = async () => {
    try {
      setLoading(true);
      const data = await accountService.getAll();
      setAccounts(data);
      setError(null);
    } catch (err: any) {
      setError('No se pudo conectar con el servidor para cargar las cuentas.');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchAccounts();
  }, []);

  return (
    <div className="container mt-4">
      <div className="d-flex justify-content-between align-items-center mb-4">
        <div>
          <h2>Banco Interno – Cuentas</h2>
          <p className="text-muted">Pantalla 1: lista de cuentas (GET /api/accounts)</p>
        </div>
        <div>
          <button className="btn btn-primary me-2 active">Cuentas</button>
          <button className="btn btn-outline-primary" onClick={onNavigateToTransfer}>
            Nueva transferencia
          </button>
        </div>
      </div>

      {error && <div className="alert alert-danger">{error}</div>}

      <div className="card shadow-sm">
        <div className="card-body">
          {loading ? (
            <div className="text-center py-3">Cargando cuentas...</div>
          ) : (
            <table className="table table-striped table-hover align-middle">
              <thead className="table-dark">
                <tr>
                  <th>ID</th>
                  <th>Titular</th>
                  <th className="text-end">Saldo (HNL)</th>
                </tr>
              </thead>
              <tbody>
                {accounts.map((acc) => (
                  <tr key={acc.id}>
                    <td>{acc.id}</td>
                    <td>{acc.holderName}</td>
                    <td className="text-end fw-bold">
                      L {acc.balance.toLocaleString('en-US', { minimumFractionDigits: 2 })}
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          )}
        </div>
      </div>

      <button className="btn btn-secondary mt-3" onClick={fetchAccounts}>
        Actualizar
      </button>
    </div>
  );
};