import { expect, test, vi } from 'vitest';
import { screen, render, fireEvent, within } from '@testing-library/svelte';

import page from './+page.svelte';

vi.mock('$lib/api/session.service', () => ({
	sessionService: { getById: vi.fn().mockResolvedValue({}), update: vi.fn().mockResolvedValue({}) }
}));

vi.mock('$lib/api/tastingNote.service', () => ({
	tastingNoteService: { findByNote: vi.fn().mockResolvedValue(['mock_grass', 'mock_hay']) }
}));

test('infusion should take temperature of the previous one', async () => {
	render(page, {
		props: {
			data: {
				session: { id: 0, infusions: [] },
				tea: { name: '' },
				selfScrolling: true,
				sessions: [],
				breadcrumbs: []
			}
		}
	});

	// Test initial state
	const addInfusion = screen.getByRole('button', { name: 'Add infusion' });
	expect(addInfusion).toBeVisible();
	expect(screen.queryByRole('button', { name: 'Select Infusion 0' })).not.toBeInTheDocument();

	// Add first infusion - temperature inpus visible but empty
	await fireEvent.click(addInfusion);
	expect(await screen.findByRole('button', { name: 'Select Infusion 1' })).toBeVisible();
	expect(await screen.findByRole('button', { name: 'Select Infusion 1' })).toHaveClass(
		'btn-primary'
	);

	const temperatureInput = screen.getByPlaceholderText('Temperature (°C)');
	expect(temperatureInput).toBeVisible();
	expect(temperatureInput).toHaveValue(null);

	// Add secand infusion - temperature still visible and empty
	await fireEvent.click(addInfusion);
	expect(await screen.findByRole('button', { name: 'Select Infusion 2' })).toBeVisible();
	expect(await screen.findByRole('button', { name: 'Select Infusion 2' })).toHaveClass(
		'btn-primary'
	);
	expect(await screen.findByRole('button', { name: 'Select Infusion 1' })).not.toHaveClass(
		'btn-primary'
	);
	expect(temperatureInput).toBeVisible();
	expect(temperatureInput).toHaveValue(null);

	// add temperature of second infusion
	await fireEvent.input(temperatureInput, { target: { value: '85' } });
	expect(temperatureInput).toHaveValue(85);

	// Add third infusion - temperature should still be visible and have the value of second infusion
	await fireEvent.click(addInfusion);
	expect(await screen.findByRole('button', { name: 'Select Infusion 3' })).toBeVisible();
	expect(await screen.findByRole('button', { name: 'Select Infusion 3' })).toHaveClass(
		'btn-primary'
	);
	expect(await screen.findByRole('button', { name: 'Select Infusion 2' })).not.toHaveClass(
		'btn-primary'
	);
	expect(temperatureInput).toBeVisible();
	expect(temperatureInput).toHaveValue(85);

	// First infusion still null
	(await screen.findByRole('button', { name: 'Select Infusion 1' })).click();
	expect(await screen.findByRole('button', { name: 'Select Infusion 1' })).toHaveClass(
		'btn-primary'
	);
	expect(await screen.findByRole('button', { name: 'Select Infusion 3' })).not.toHaveClass(
		'btn-primary'
	);
	expect(temperatureInput).toBeVisible();
	expect(temperatureInput).toHaveValue(null);
});

test('test tasting note modal', async () => {
	render(page, {
		props: {
			data: {
				session: { id: 0, infusions: [] },
				tea: { name: '' },
				selfScrolling: true,
				sessions: [],
				breadcrumbs: []
			}
		}
	});

	expect(screen.queryByRole('group', { name: 'Eye' })).not.toBeInTheDocument();
	expect(screen.queryByRole('group', { name: 'Nose' })).not.toBeInTheDocument();

	const addTastingNote = screen.getByRole('button', { name: 'Add your first tasting note' });
	expect(addTastingNote).toBeVisible();
	expect(screen.queryByRole('radio', { name: 'Eye' })).not.toBeInTheDocument();

	await fireEvent.click(addTastingNote);
	expect(screen.getByRole('radio', { name: 'Eye' })).toBeVisible();
	expect(screen.getByRole('radio', { name: 'Eye' })).toBeChecked();

	const noteInput = screen.getByPlaceholderText('Search Note (Enter to add)');
	expect(noteInput).toBeVisible();
	expect(screen.queryByRole('button', { name: 'grass' })).not.toBeInTheDocument();

	// First input should add
	await fireEvent.input(noteInput, { target: { value: 'grass' } });
	expect(noteInput).toHaveValue('grass');
	await fireEvent.keyDown(noteInput, { key: 'Enter' });
	expect(noteInput).toHaveValue('');

	expect(screen.getByRole('button', { name: 'grass' })).toBeVisible();

	// Second input should remove
	await fireEvent.input(noteInput, { target: { value: 'grass' } });
	expect(noteInput).toHaveValue('grass');
	await fireEvent.keyDown(noteInput, { key: 'Enter' });
	expect(noteInput).toHaveValue('');

	expect(screen.queryByRole('button', { name: 'grass' })).not.toBeInTheDocument();

	// Clicking the note should delete it
	await fireEvent.input(noteInput, { target: { value: 'grass' } });
	expect(noteInput).toHaveValue('grass');
	await fireEvent.keyDown(noteInput, { key: 'Enter' });
	expect(noteInput).toHaveValue('');

	expect(screen.getByRole('button', { name: 'grass' })).toBeVisible();
	await fireEvent.click(screen.getByRole('button', { name: 'grass' }));
	expect(screen.queryByRole('button', { name: 'grass' })).not.toBeInTheDocument();

	// Clicking the suggestion should add it
	expect(
		await screen.findByRole('button', { name: 'mock_grass', pressed: undefined })
	).toBeVisible();
	await fireEvent.click(screen.getByRole('button', { name: 'mock_grass' }));
	expect(screen.getByRole('button', { name: 'mock_grass', pressed: true })).toBeVisible();

	// Closing should reveal the category and note
	await fireEvent(screen.getByRole('dialog'), new Event('close'));
	expect(
		screen.queryByRole('button', { name: 'mock_grass', pressed: true })
	).not.toBeInTheDocument();

	const category = within(screen.getByRole('group', { name: 'Eye' }));
	const subcategory = within(category.getByRole('group', { name: 'Dry Leaf' }));
	expect(subcategory.getByText('mock_grass')).toBeVisible();
	expect(screen.queryByRole('group', { name: 'Nose' })).not.toBeInTheDocument();
	expect(subcategory.getByRole('button', { name: 'Add tasting notes' })).toBeVisible();
	expect(addTastingNote).not.toBeInTheDocument();
});

// Todo: tasting note preselection, category / subcategory switching
// Todo: header logic
// Todo: timer logic
// Todo: rinse
