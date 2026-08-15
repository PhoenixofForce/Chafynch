import { expect, test } from 'vitest';
import { screen, render } from '@testing-library/svelte';

import TastingNoteModal from './TastingNoteModal.svelte';

test('first test', async () => {
	const { component } = render(TastingNoteModal, {
		props: { categories: [{ name: 'foo', subCategories: ['bar-1', 'bar-2'] }] }
	});
	component.open();
	expect(await screen.findByTestId('Category-Picker')).toBeVisible();
});
