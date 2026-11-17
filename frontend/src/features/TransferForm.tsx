// ./src/features/TransferForm.tsx
import React, { useEffect, useState } from 'react';
import { accountService } from '../services/accountService';
import { transferService } from '../services/transferService';
import type { Account, TransferResponse } from '../types/banking';

interface TransferFormProps {
  onNavigateToAccounts: () => void;
}

export const TransferForm: React.FC<TransferFormProps> = ({ onNavigateToAccounts }) => {
  const [accounts, setAccounts] = useState<Account[]>([]);
  const [sourceId, setSourceId] = useState<number>(1001);
  const [destinationId, setDestinationId] = useState<number>(1002);
  const [amount, setAmount] = useState<string>('');
  
  const [successResult, setSuccessResult] = useState<TransferResponse | null>(null);
  const [errorMessage, setErrorMessage] = useState<string | null>(null);
  const [loading, setLoading] = useState<boolean>(false);

  useEffect(() => {
    accountService.getAll()
      .then((data) => setAccounts(data))
      .catch(() => setErrorMessage('Error al cargar la lista de cuentas.'));
  }, []);

  const handleTransfer = async (e: React.FormEvent) => {
    e.preventDefault();
    setErrorMessage(null);
    setSuccessResult(null);

    if (sourceId === destinationId) {
      setErrorMessage('La cuenta de origen y destino no pueden ser la misma.');
      return;
    }

    try {
      setLoading(true);
      const data = await transferService.execute({
        sourceAccountId: sourceId,
        destinationAccountId: destinationId,
        amount: parseFloat(amount),
      });
      setSuccessResult(data);
      setAmount('');
    } catch (err: any) {
      const msg = err.response?.data?.message || 'Ocurrió un error al procesar la transferencia.';
      setErrorMessage(msg);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="container mt-4">
      <div className="d-flex justify-content-between align-items-center mb-4">
        <div>
          <h2>Nueva transferencia</h2>
          <p className="text-muted">Pantalla 2 y 3: formulario y resultados (POST /api/transfers)</p>
        </div>
        <div>
          <button className="btn btn-outline-primary me-2" onClick={onNavigateToAccounts}>
            Cuentas
          </button>
          <button className="btn btn-primary active">Nueva transferencia</button>
        </div>
      </div>

      {errorMessage && (
        <div className="alert alert-danger border-danger">
          <strong>✕ Transferencia rechazada</strong>
          <div>{errorMessage}</div>
        </div>
      )}

      {successResult && (
        <div className="alert alert-success border-success mb-4">
          <strong>✓ Transferencia realizada</strong>
          <div className="mb-3">
            Transferencia No. {successResult.transferId} · L {successResult.amount.toFixed(2)} de {successResult.sourceAccountId} a {successResult.destinationAccountId}
          </div>
          <table className="table table-sm table-bordered bg-white">
            <thead>
              <tr>
                <th>Cuenta</th>
                <th>Saldo anterior</th>
                <th>Saldo nuevo</th>
              </tr>
            </thead>
            <tbody>
              <tr>
                <td>{successResult.sourceAccountId} – {successResult.sourceHolderName}</td>
                <td>L {successResult.sourcePreviousBalance.toFixed(2)}</td>
                <td>L {successResult.sourceNewBalance.toFixed(2)}</td>
              </tr>
              <tr>
                <td>{successResult.destinationAccountId} – {successResult.destinationHolderName}</td>
                <td>L {successResult.destinationPreviousBalance.toFixed(2)}</td>
                <td>L {successResult.destinationNewBalance.toFixed(2)}</td>
              </tr>
            </tbody>
          </table>
          <div className="d-flex gap-2">
            <button className="btn btn-sm btn-outline-success" onClick={() => setSuccessResult(null)}>
              Nueva transferencia
            </button>
            <button className="btn btn-sm btn-secondary" onClick={onNavigateToAccounts}>
              Ver cuentas
            </button>
          </div>
        </div>
      )}

      {!successResult && (
        <div className="card shadow-sm" style={{ maxWidth: '600px' }}>
          <div className="card-body">
            <form onSubmit={handleTransfer}>
              <div className="mb-3">
                <label className="form-label">Cuenta origen</label>
                <select
                  className="form-select"
                  value={sourceId}
                  onChange={(e) => setSourceId(Number(e.target.value))}
                >
                  {accounts.map((acc) => (
                    <option key={acc.id} value={acc.id}>
                      {acc.id} – {acc.holderName} (L {acc.balance.toFixed(2)})
                    </option>
                  ))}
                </select>
              </div>

              <div className="mb-3">
                <label className="form-label">Cuenta destino</label>
                <select
                  className="form-select"
                  value={destinationId}
                  onChange={(e) => setDestinationId(Number(e.target.value))}
                >
                  {accounts.map((acc) => (
                    <option key={acc.id} value={acc.id}>
                      {acc.id} – {acc.holderName} (L {acc.balance.toFixed(2)})
                    </option>
                  ))}
                </select>
              </div>

              <div className="mb-3">
                <label className="form-label">Monto (HNL)</label>
                <input
                  type="number"
                  step="0.01"
                  min="0.01"
                  className="form-control"
                  placeholder="0.00"
                  value={amount}
                  onChange={(e) => setAmount(e.target.value)}
                  required
                />
              </div>

              <div className="d-flex gap-2">
                <button type="submit" className="btn btn-primary" disabled={loading}>
                  {loading ? 'Procesando...' : 'Transferir'}
                </button>
                <button type="button" className="btn btn-outline-secondary" onClick={onNavigateToAccounts}>
                  Cancelar
                </button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  );
};