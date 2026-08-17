import { expect, test, vi } from 'vitest';
import { screen, render } from '@testing-library/svelte';

import TastingNoteModal from './TastingNoteModal.svelte';

vi.mock('$lib/api/tastingNote.service', () => ({
	tastingNoteService: { findByNote: vi.fn().mockResolvedValue([]) }
}));

test('first test', async () => {
	const { component } = render(TastingNoteModal, {
		props: { categories: [{ name: 'foo', subCategories: ['bar-1', 'bar-2'] }] }
	});
	component.open();
	expect(await screen.findByTestId('Category-Picker')).toBeVisible();
});
