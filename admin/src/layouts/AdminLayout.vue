<template>
  <div class="shell">
    <aside class="side">
      <p class="brand">DEMO Admin<span>只读运营查询</span></p>
      <nav class="nav">
        <RouterLink to="/dashboard">Dashboard</RouterLink>
        <RouterLink to="/users">Users</RouterLink>
        <RouterLink to="/transactions">Transactions</RouterLink>
        <RouterLink to="/status">System</RouterLink>
      </nav>
    </aside>
    <main class="main">
      <RouterView />
    </main>
  </div>
</template>
