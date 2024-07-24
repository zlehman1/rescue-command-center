<template>
  <footer class="footer">
    <div class="content has-text-centered">
      <p class="footer-link">
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
  background-color: var(--background-footer-color);
  padding: 1rem 0;
  position: fixed;
  width: 100%;
  bottom: 0;
  text-align: center;
}

.footer .content {
  font-size: 0.9rem;
}

.footer a {
  text-decoration: none;
}

.footer-link{
  color: var(--background-footer-a-color);
}

.footer a:hover {
  text-decoration: underline;
}

.footer a:visited {
  color: var(--background-footer-a-color);
}

</style>

<style>
:root {
  --background-footer-color-light: #f5f5f5;
  --background-footer-color-dark: #8e8e8e;
  --background-footer-a-color-light: blue;
  --background-footer-a-color-dark: white;
}

[data-theme="light"] {
  --background-footer-color: var(--background-footer-color-light);
  --background-footer-a-color: var(--background-footer-a-color-light);
}

[data-theme="dark"] {
  --background-footer-color: var(--background-footer-color-dark);
  --background-footer-a-color: var(--background-footer-a-color-dark);
}
</style>