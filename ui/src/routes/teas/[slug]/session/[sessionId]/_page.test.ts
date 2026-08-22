import { expect, test, vi } from 'vitest';
import { screen, render, fireEvent } from '@testing-library/svelte';

import page from './+page.svelte';

vi.mock('$lib/api/session.service', () => ({
	sessionService: { getById: vi.fn().mockResolvedValue({}), update: vi.fn().mockResolvedValue({}) }
}));

vi.mock('$lib/api/tastingNote.service', () => ({
	tastingNoteService: { findByNote: vi.fn().mockResolvedValue([]) }
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
	expect(await screen.findByRole('button', { name: 'Select Infusion 0' })).toBeVisible();
	expect(await screen.findByRole('button', { name: 'Select Infusion 0' })).toHaveClass(
		'btn-primary'
	);

	const temperatureInput = screen.getByPlaceholderText('Temperature (°C)');
	expect(temperatureInput).toBeVisible();
	expect(temperatureInput).toHaveValue(null);

	// Add secand infusion - temperature still visible and empty
	await fireEvent.click(addInfusion);
	expect(await screen.findByRole('button', { name: 'Select Infusion 1' })).toBeVisible();
	expect(await screen.findByRole('button', { name: 'Select Infusion 1' })).toHaveClass(
		'btn-primary'
	);
	expect(await screen.findByRole('button', { name: 'Select Infusion 0' })).not.toHaveClass(
		'btn-primary'
	);
	expect(temperatureInput).toBeVisible();
	expect(temperatureInput).toHaveValue(null);

	// add temperature of second infusion
	await fireEvent.input(temperatureInput, { target: { value: '85' } });
	expect(temperatureInput).toHaveValue(85);

	// Add third infusion - temperature should still be visible and have the value of second infusion
	await fireEvent.click(addInfusion);
	expect(await screen.findByRole('button', { name: 'Select Infusion 2' })).toBeVisible();
	expect(await screen.findByRole('button', { name: 'Select Infusion 2' })).toHaveClass(
		'btn-primary'
	);
	expect(await screen.findByRole('button', { name: 'Select Infusion 1' })).not.toHaveClass(
		'btn-primary'
	);
	expect(temperatureInput).toBeVisible();
	expect(temperatureInput).toHaveValue(85);

	// First infusion still null
	(await screen.findByRole('button', { name: 'Select Infusion 0' })).click();
	expect(await screen.findByRole('button', { name: 'Select Infusion 0' })).toHaveClass(
		'btn-primary'
	);
	expect(await screen.findByRole('button', { name: 'Select Infusion 2' })).not.toHaveClass(
		'btn-primary'
	);
	expect(temperatureInput).toBeVisible();
	expect(temperatureInput).toHaveValue(null);
});

// Todo: header logic
// Todo: modal logic
// Todo: timer logic
// Todo: rinse
