<template>
  <div class="shell">
    <aside class="side">
      <p class="brand">DEMO Admin<span>本地 Demo</span></p>
      <nav class="nav">
        <RouterLink to="/dashboard">Dashboard</RouterLink>
        <RouterLink to="/users">Users</RouterLink>
        <RouterLink to="/deposits">Deposits</RouterLink>
        <RouterLink to="/withdrawals">Withdrawals</RouterLink>
        <RouterLink to="/transactions">Transactions</RouterLink>
        <RouterLink to="/status">System</RouterLink>
      </nav>
    </aside>
    <main class="main">
      <RouterView />
    </main>
  </div>
</template>
