/* eslint-disable */
export function trackPageview(pageURL) {
  if (_hmt) {
    _hmt.push(['_trackPageview', pageURL])
  }
}
/* eslint-enable */