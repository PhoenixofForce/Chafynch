import { expect, test } from 'vitest';
import { screen, render } from '@testing-library/svelte';

import TastingNoteModal from './TastingNoteModal.svelte';

test('first test', async () => {
	const { component } = render(TastingNoteModal);
	component.open();
	expect(await screen.findByTestId('Category-Picker')).toBeVisible();
});
