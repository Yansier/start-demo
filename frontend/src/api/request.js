/**
 * 后续接入 Java API 时，只替换这里的请求实现。
 * 当前阶段业务模块走 mock，不发起真实网络请求。
 */
export function request() {
  return Promise.reject(new Error('Java API 尚未接入'))
}
