# Legal Documents Templates

This directory contains template legal documents required for publishing the BubbleTea app on Google Play Store.

## 📄 Available Templates

### Privacy Policy
- **[PRIVACY_POLICY_TEMPLATE.md](PRIVACY_POLICY_TEMPLATE.md)** - English version
- **[PRIVACY_POLICY_TEMPLATE_KO.md](PRIVACY_POLICY_TEMPLATE_KO.md)** - Korean version (한국어)

### Terms of Service
- **[TERMS_OF_SERVICE_TEMPLATE.md](TERMS_OF_SERVICE_TEMPLATE.md)** - English version
- **[TERMS_OF_SERVICE_TEMPLATE_KO.md](TERMS_OF_SERVICE_TEMPLATE_KO.md)** - Korean version (한국어)

## 🚨 Important: These are TEMPLATES

**You MUST customize these documents before publishing:**

1. Replace all placeholder text:
   - `[Insert Date]` / `[날짜를 입력하세요]`
   - `[Developer/Company Name]` / `[개발자/회사명]`
   - `[Contact Email]` / `[연락처 이메일]`
   - `[Website URL]` / `[웹사이트 URL]`
   - `[Your Country/State]` / `[귀하의 국가/주]`
   - `[Your Jurisdiction]` / `[귀하의 관할권]`
   - `[Physical Address, if required]` / `[실제 주소, 필요한 경우]`

2. Review all content to ensure it accurately reflects your app's practices

3. **Seek legal advice** - These templates are for reference only and do not substitute professional legal advice

## 📤 Publishing Requirements

### Where to Host

These documents must be:
- Publicly accessible via HTTPS URL
- Available in a web browser
- Not behind a login or paywall

### Recommended Hosting Options

1. **GitHub Pages** (Free)
   - Create a new repository for your privacy policy
   - Enable GitHub Pages in repository settings
   - URL will be: `https://yourusername.github.io/repository-name/`

2. **Firebase Hosting** (Free)
   - Simple deployment with Firebase CLI
   - Custom domain support

3. **Netlify** (Free)
   - Drag-and-drop deployment
   - Automatic SSL

### Converting to HTML

If you need HTML versions instead of Markdown:

```bash
# Using pandoc (if installed)
pandoc PRIVACY_POLICY_TEMPLATE.md -o privacy-policy.html
pandoc TERMS_OF_SERVICE_TEMPLATE.md -o terms-of-service.html
```

Or use online converters:
- https://markdowntohtml.com/
- https://www.browserling.com/tools/markdown-to-html

## 🔗 Required for Google Play Store

Google Play Console requires:
- **Privacy Policy URL** - Required field, cannot publish without it
- **Terms of Service URL** - Recommended but optional

Both URLs will be:
- Visible to users in your app listing
- Used by Google to verify compliance
- Required to remain accessible as long as your app is published

## ✅ Checklist Before Publishing

- [ ] Customized all placeholder text
- [ ] Reviewed content for accuracy
- [ ] Consulted with legal professional (recommended)
- [ ] Hosted on public HTTPS URL
- [ ] Tested URL accessibility
- [ ] Updated URL in Google Play Console
- [ ] Linked from within your app (recommended)

## 📞 Need Help?

Refer to:
- **[APP_RELEASE_CHECKLIST.md](../APP_RELEASE_CHECKLIST.md)** - Full release preparation guide
- **[QUICK_START_GUIDE_KO.md](../QUICK_START_GUIDE_KO.md)** - Quick start guide (Korean)

## ⚖️ Legal Disclaimer

**IMPORTANT**: These templates are provided for informational purposes only and do not constitute legal advice. Laws vary by jurisdiction, and you should consult with a qualified attorney to ensure your privacy policy and terms of service comply with all applicable laws and regulations.

The templates are based on common practices for Android apps but may not cover all requirements specific to your app or jurisdiction.

---

**Last Updated**: [Add current date when you customize these documents]
