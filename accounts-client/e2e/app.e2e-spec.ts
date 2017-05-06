import { AccountsClientPage } from './app.po';

describe('accounts-client App', () => {
  let page: AccountsClientPage;

  beforeEach(() => {
    page = new AccountsClientPage();
  });

  it('should display message saying app works', () => {
    page.navigateTo();
    expect(page.getParagraphText()).toEqual('app works!');
  });
});
