import '@testing-library/jest-dom/vitest';

HTMLDialogElement.prototype.showModal = function () {
	this.open = true;
};
HTMLDialogElement.prototype.close = function (returnValue?: string) {
	this.open = false;
	if (returnValue !== undefined) this.returnValue = returnValue;
	this.dispatchEvent(new Event('close'));
};
Element.prototype.scrollTo = () => {};
Element.prototype.scrollIntoView = () => {};
