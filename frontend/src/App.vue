<script>
import { useUserStore } from './stores/user'

export default {
  onLaunch() {
    useUserStore().hydrate()
  }
}
</script>

<style>
@import './styles/theme.css';

/* #ifdef H5 */
html,
body,
#app {
  min-height: 100%;
  background: #04060f;
  overflow-x: hidden;
}

uni-app,
uni-page,
uni-page-wrapper,
uni-page-body {
  width: 100%;
  max-width: 430px;
  min-height: 100vh;
  margin: 0 auto;
  background: transparent;
}
/* #endif */
</style>
