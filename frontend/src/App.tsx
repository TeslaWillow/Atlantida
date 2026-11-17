// ./src/App.tsx
import { useState } from 'react';
import { AccountList } from './features/AccountList';
import { TransferForm } from './features/TransferForm';

function App() {
  const [currentView, setCurrentView] = useState<'accounts' | 'transfer'>('accounts');

  return (
    <div className="bg-light min-vh-100 pb-5">
      {currentView === 'accounts' ? (
        <AccountList onNavigateToTransfer={() => setCurrentView('transfer')} />
      ) : (
        <TransferForm onNavigateToAccounts={() => setCurrentView('accounts')} />
      )}
    </div>
  );
}

export default App;