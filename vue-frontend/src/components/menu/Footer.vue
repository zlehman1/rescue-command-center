<template>
  <footer class="footer">
    <div class="content has-text-centered">
      <p>
        &copy; 2024 <a href="https://www.w-hs.de" target="_blank">Rescue Command Center</a>
      </p>
    </div>
  </footer>
</template>

<script>
export default {
  name: 'FooterComponent'
};
</script>

<style scoped>
.footer {
  background-color: #f5f5f5;
  padding: 1rem 0;
  position: fixed;
  width: 100%;
  bottom: 0;
  text-align: center; /* Zentriert den Text im Footer */
}

.footer .content {
  font-size: 0.9rem;
}

.footer a {
  color: #3273dc;
  text-decoration: none;
}

.footer a:hover {
  text-decoration: underline;
}
</style>